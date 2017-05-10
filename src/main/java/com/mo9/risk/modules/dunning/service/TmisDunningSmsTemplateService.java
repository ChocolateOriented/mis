package com.mo9.risk.modules.dunning.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.batik.bridge.UpdateManagerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mo9.risk.modules.dunning.dao.TMisDunningPeopleDao;
import com.mo9.risk.modules.dunning.dao.TRiskBuyerPersonalInfoDao;
import com.mo9.risk.modules.dunning.dao.TmisDunningSmsTemplateDao;
import com.mo9.risk.modules.dunning.entity.TMisDunningOrder;
import com.mo9.risk.modules.dunning.entity.TMisDunningPeople;
import com.mo9.risk.modules.dunning.entity.TMisDunningTask;
import com.mo9.risk.modules.dunning.entity.TRiskBuyerPersonalInfo;
import com.mo9.risk.modules.dunning.entity.TmisDunningSmsTemplate;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;



/**
 * 
 * 短信模板service
 * @author jwchi
 *
 */

@Service
@Transactional(readOnly = true)
public class TmisDunningSmsTemplateService extends CrudService<TmisDunningSmsTemplateDao, TmisDunningSmsTemplate> {
	
	
	@Autowired
	TmisDunningSmsTemplateDao tsTemplateDao;
	@Autowired
	TMisDunningPeopleDao tmisPeopleDao;
	@Autowired
	TRiskBuyerPersonalInfoDao tbuyerDao;

	@Autowired
	private TMisContantRecordService tcrService;
	
	public Page<TmisDunningSmsTemplate> findOrderPageList(Page<TmisDunningSmsTemplate> page, TmisDunningSmsTemplate entity) {
		
		entity.setPage(page);
		page.setList(dao.findSmsPageList(entity));
		return page;
	}
	/**
	 * 更新短信模板和催收历史中含短信模板信息
	 * @param tDunningSmsTemplate
	 */
	@Transactional(readOnly = false)
	public void updateList(TmisDunningSmsTemplate tDunningSmsTemplate){
		if("labourSend".equals(tDunningSmsTemplate.getSendMethod())){
			tDunningSmsTemplate.setSendTime(null);
		}
		
		   super.save(tDunningSmsTemplate);
	       tcrService.updateTmisContatRwcord(tDunningSmsTemplate);
           
	}
    
	  //根据逾期天数对应的模板
	   public List<TmisDunningSmsTemplate> findSmsTemplate(String contactType,Date repaymentDate,TMisDunningOrder order,TMisDunningTask task){
		   
		   TmisDunningSmsTemplate template=new TmisDunningSmsTemplate();
		  
		  int overdayas =  (int) DateUtils.getDistanceOfTwoDate(repaymentDate, new Date());
		  String acceptType=""; 
		  if(!"".equals(contactType)){
		  if("self".equalsIgnoreCase(contactType)){
			  acceptType="self";
		  }else{
			  acceptType="others";
		  }
		  } 
		  List<TmisDunningSmsTemplate> findList = tsTemplateDao.findListSMSTemplate(overdayas,acceptType);
		  
		 if(findList.size()!=0){
		  TmisDunningSmsTemplate tSmsTemplate=findList.get(0);
		  
		  String smsCotent = tSmsTemplate.getSmsCotent();
		  String cousmscotent = cousmscotent(smsCotent,order,task);
		  tSmsTemplate.setSmsCotent(cousmscotent); 
		 } 
		  return findList;
		}
	   
	 
		  /**
		   * 
		   * 填充短信内容
		   * @param smsCotent
		   * @return
		   */
        public String cousmscotent(String smsCotent,TMisDunningOrder order,TMisDunningTask task){
        	
        	String dunningpeopleid = task.getDunningpeopleid();
        	
        	TMisDunningPeople tMisDunningPeople = tmisPeopleDao.get(dunningpeopleid);
        	
        	TRiskBuyerPersonalInfo buyerInfeo= tbuyerDao.getBuyerInfoByDealcode(order.getDealcode());
        	
        	SimpleDateFormat ss=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	
        	Map<String, Object> map = new HashMap<String, Object>();
        	
        	String  platform="";
        	String  creditAmount="";
        	
        	if(smsCotent.contains("${platform}")){
        		if("weixin".equals(order.getPlatform())){
        			platform="微信";
        		}
        		if("app".equals(order.getPlatform())){
        			platform="APP";
        		}
        		if("liulanqi".equals(order.getPlatform())){
        			platform="浏览器";
        		}
        		
        	smsCotent=smsCotent.replace("${platform}", platform);
        		
        	}
        	
        	if(smsCotent.contains("${realName}")){
        		smsCotent=smsCotent.replace("${realName}",buyerInfeo.getRealName());
        	}
        	if(smsCotent.contains("${sex}")){
        		smsCotent=smsCotent.replace("${sex}",buyerInfeo.getSex());
        	}
        	if(smsCotent.contains("${idCard}")){
        		String idcard = buyerInfeo.getIdcard();
        		
        		StringBuilder sb=new StringBuilder(idcard);
        		sb= sb.replace(3, 15, "*********");
        		
        		smsCotent=smsCotent.replace("${idCard}",sb.toString());
        	}
        	if(smsCotent.contains("${creditamount}")){
        		creditAmount=String.valueOf((Double.valueOf(buyerInfeo.getCreditAmount()).doubleValue()/100D));
        		smsCotent=smsCotent.replace("${creditamount}",creditAmount);
        	}
        	if(smsCotent.contains("${extensionNumber}")){
        		smsCotent=smsCotent.replace("${extensionNumber}",tMisDunningPeople.getExtensionNumber());
        	}
        	if(smsCotent.contains("${creadateTime}")){
        		
        		smsCotent=smsCotent.replace("${creadateTime}",ss.format(buyerInfeo.getCreateTime()));
        	}
        	if(smsCotent.contains("${repaymentTime}")){
        		smsCotent=smsCotent.replace("${repaymentTime}",ss.format(buyerInfeo.getRepaymentTime()));
        	}
        	if(smsCotent.contains("${overduedays}")){
        		smsCotent=smsCotent.replace("${overduedays}",buyerInfeo.getOverdueDays());
        	}
        	if(smsCotent.contains("${todayDate}")){
        		smsCotent=smsCotent.replace("${todayDate}",ss.format(new Date()));
        	}
        	
        	return smsCotent;
        }	
        	
    
	
}
