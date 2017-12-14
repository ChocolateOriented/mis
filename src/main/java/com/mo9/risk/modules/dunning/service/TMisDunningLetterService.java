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
	
	public Page<TMisDunningLetter> findPageList(Page<TMisDunningLetter> page, TMisDunningLetter entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}
	/**
	 * 同步逾期60+案件
	 */
	@Scheduled(cron = "0 20 3 * * ?")
	@Transactional(readOnly = false)
	public void synDealcode(){
		List<TMisDunningLetter> synDealcodeList=dao.findSynDealcode();
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
			if("寄出".equals(tMisDunningLetter.getSendResultSting())){
				tMisDunningLetter.setSendResult(SendResult.POSTED);
			}else if("退回".equals(tMisDunningLetter.getSendResultSting())){
				tMisDunningLetter.setSendResult(SendResult.BACKED);
			}else{
				message.append("第"+(i+1)+"条状态不对,请检查");
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
		boolean sendMailBOl = sendMail(uuid);
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
	public boolean sendMail(String uuid){
		//收件人
		StringBuilder receiver = new StringBuilder();
		List<Dict> emails = DictUtils.getDictList("migration_rate_report_email");
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
		StringBuilder content = new StringBuilder();
		content.append("<table  border='1' cellspacing='0' bordercolor='#b0b0b0' style='text-align: center'>");
		content.append("<tr><th>时间</th><th>操作</th></tr>");
		content.append("<tr><td>"+data+"</td><td><a href='http://localhost/mis/letter/downLoad?identity="+uuid+"'>下载</a></td></tr>");
		content.append("</table>");
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
