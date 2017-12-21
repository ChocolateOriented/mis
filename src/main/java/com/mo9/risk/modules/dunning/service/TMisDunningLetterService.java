package com.mo9.risk.modules.dunning.service;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.bean.TMisDunningLetterDownLoad;
import com.mo9.risk.modules.dunning.dao.TMisDunningLetterDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningLetter;
import com.mo9.risk.modules.dunning.entity.TMisDunningLetter.SendResult;
import com.mo9.risk.util.DateUtils;
import com.mo9.risk.util.MailSender;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 东港信函Controller
 * @author chijw
 * @version 2017-12-07
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningLetterService extends CrudService<TMisDunningLetterDao, TMisDunningLetter>  {
	
	@Autowired
	protected Validator validator;
	/**
	 * 分页查询
	 * @param page
	 * @param entity
	 * @return
	 */
	public Page<TMisDunningLetter> findPageList(Page<TMisDunningLetter> page, TMisDunningLetter entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}
	/**
	 * 同步逾期历史60+(可配置)案件
	 * 
	 */
	@Transactional(readOnly = false)
	public void synDealcode(){
		String synDay = DictUtils.getDictValue("synDay", "letter", "");
		if(StringUtils.isEmpty(synDay)){
			logger.warn("数据字典未配置需同步的案件天数");
			return;
		}
		Integer daySyn;
		try{
			daySyn = Integer.valueOf(synDay);
		}catch(Exception e){
			logger.warn("数据字典配置同步的案件天数错误",e);
			return;
		}
		logger.info("开始同步历史信函的案件");
		List<TMisDunningLetter> synDealcodeList=dao.findSynDealcode(daySyn,"history");
		saveSynDealcode(synDealcodeList);
	}
	
	/**
	 * 同步逾期60(可配置)当天案件
	 * 
	 */
	@Scheduled(cron = "0 30 3 * * ?")
	@Transactional(readOnly = false)
	public void synDealcodeToday(){
		String synDay = DictUtils.getDictValue("synDay", "letter", "");
		if(StringUtils.isEmpty(synDay)){
			logger.warn("数据字典未配置需同步的案件天数");
			return;
		}
		Integer daySyn;
		try{
			daySyn = Integer.valueOf(synDay);
		}catch(Exception e){
			logger.warn("数据字典配置同步的案件天数错误",e);
			return;
		}
		logger.info("开始同步当天信函的案件");
		List<TMisDunningLetter> synDealcodeList=dao.findSynDealcode(daySyn,"today");
		saveSynDealcode(synDealcodeList);
	}
	//保存同步的案件
	private void saveSynDealcode(List<TMisDunningLetter> synDealcodeList) {
		for (TMisDunningLetter tMisDunningLetter : synDealcodeList) {
			tMisDunningLetter.setSendResult(SendResult.BESEND);
			tMisDunningLetter.setResultDate(new Date());
			tMisDunningLetter.preInsert();
		}
		if(synDealcodeList != null && synDealcodeList.size()>0){
			dao.saveList(synDealcodeList);
			dao.saveLogList(synDealcodeList);
		}
	}
	/**
	 * 导入信函状态
	 * @param list
	 * @param message
	 * @return
	 */
	@Transactional(readOnly=false)
	public boolean batchUpdate(List<TMisDunningLetter> list, StringBuilder message) {
		
		for (int i = 0; i < list.size(); i++) {
			TMisDunningLetter tMisDunningLetter = list.get(i);
			String dealcode = tMisDunningLetter.getDealcode();
			String postCode = tMisDunningLetter.getPostCode();
			if(StringUtils.isEmpty(dealcode)||StringUtils.isEmpty(postCode)){
				message.append("第"+(i+1)+"条内容为空,请检查");
				return false;
			}
			if("寄出".equals(tMisDunningLetter.getSendResultSting())){
				tMisDunningLetter.setSendResult(SendResult.POSTED);
			}else if("退回".equals(tMisDunningLetter.getSendResultSting())){
				tMisDunningLetter.setSendResult(SendResult.BACKED);
			}else{
				message.append("第"+(i+1)+"条状态不对,请检查");
				return false;
			}
			TMisDunningLetter letter=dao.findLetterByDealcode(dealcode);
			if(!"待寄出".equals(letter.getSendResultText())){
				message.append("第"+(i+1)+"条订单状态不为待寄出不能导入,请检查");
				return false;
			}
			try {
				BeanValidators.validateWithException(validator,tMisDunningLetter);
			} catch (ConstraintViolationException ex) {
				message.append("第"+(i+1)+"条邮编格式不对,请检查");
				return false;
			}
			tMisDunningLetter.setResultDate(new Date());
			tMisDunningLetter.preInsert();
			dao.updateStatus(tMisDunningLetter);
		}
		dao.saveLogList(list);
		return true;
	}
	/**
	 * 发送信函邮件
	 * @param sendLetterDealcodes
	 * @param message
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean sendLetters(List<String> sendLetterDealcodes)  {
		List<TMisDunningLetter>  list=dao.findSendList(sendLetterDealcodes);
		if(list==null||list.isEmpty()){
			logger.warn("未查到数据");
			return false;
		}
		//要发送信函的邮件
		String uuid=IdGen.uuid();
		logger.info("邮件下载内容的标识:"+uuid);
		boolean sendMailBOl = sendMail(uuid,list.size());
		if(!sendMailBOl){
			return false;
		}
		//更新信函状态
		Date date=new Date();
		for (TMisDunningLetter tMisDunningLetter : list) {
			tMisDunningLetter.setSendResult(SendResult.BEPOST);
			tMisDunningLetter.setDownLoadFlag(uuid);
			tMisDunningLetter.setSendDate(date);
			tMisDunningLetter.setResultDate(date);
			tMisDunningLetter.preInsert();
			dao.updateStatus(tMisDunningLetter);
		}
		dao.saveLogList(list);
		return true;
	}
	//发送信函
	public boolean sendMail(String uuid,int countLetter){
		//收件人
		StringBuilder receiver = new StringBuilder();
		List<Dict> emails = DictUtils.getDictList("letter_receiver");
		for (Dict email: emails) {
			receiver.append(email.getValue()+",");
		}
		
		if (StringUtils.isBlank(receiver)){
			logger.info("信函发送失败, 未配置收件人邮箱");
			return false; 
		}
		logger.info("信函发送邮件至" + receiver);
		//发送邮件
		MailSender mailSender = new MailSender(receiver.toString());
		String data = DateUtils.getDate("MM月dd日");
		mailSender.setSubject(data+"的信函");
		String url = DictUtils.getDictValue("misUrl", "orderUrl", "");
		StringBuilder content = new StringBuilder();
		content.append("<table width='600px'>");
		content.append("<tr><td ><img height='88px' width='600px' src='"+url+"/static/images/donggangtitle.png'/><p></td></tr>");
		content.append("<tr><td ><p><font style='font-weight:bold;' color='#4D4D4D' size='4'>Dear 东港伙伴 您好:</font></p></td></tr>");
		content.append("<tr><td ><p><font color='#4D4D4D' size='3'>我司本次发送信函明细如下,请查收:</font></p></td></tr>");
		content.append("</table >");
		content.append("<table width='600px' border='1' cellspacing='0' bordercolor='#b0b0b0' style='text-align: center'>");
		content.append("<tr ><th height='40px' width='80px'>序号</th><th>信函总量</th><th>时间</th><th>操作</th></tr>");
		content.append("<tr><td>1</td><td>"+countLetter+"</td><td height='40px'>"+data+"</td><td><a href='"+url+"/letter/downLoad?identity="+uuid+"'>下载</a></td></tr>");
		content.append("</table>");
		content.append("<div><p><font color='#4D4D4D' size='3'>如果对信函数据存疑,可直接回复邮件.</font></p></div>");
		content.append("<div><p><font color='#4D4D4D' size='3'>顺祝商祺!</font></p></div>");
		content.append("<div><p><img height='100px' width='600px' src='"+url+"/static/images/mo9subjectend.png'/></p></div>");
		
		mailSender.setContent(content.toString());
		//发送
		try {
			mailSender.sendMail();
		} catch (Exception e) {
			logger.warn("信函邮件发送失败!", e);
			return false; 
		}
		logger.info("信函邮件发送成功");
		return true;
	}
	
	/**
	 * 下载要寄邮件的信函
	 * @param identity
	 * @return
	 */
	public List<TMisDunningLetterDownLoad> lettersDownLoad(String identity){
		return dao.lettersDownLoad(identity);
	}
}
