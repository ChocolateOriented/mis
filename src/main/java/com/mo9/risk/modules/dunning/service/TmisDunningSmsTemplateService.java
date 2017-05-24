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

//	@Autowired
//	private TMisContantRecordService tcrService;
	
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
		if("".equals(tDunningSmsTemplate.getNumbefore())||tDunningSmsTemplate.getNumbefore()==null){
			tDunningSmsTemplate.setNumbefore(-9999);
		}
		if("".equals(tDunningSmsTemplate.getNumafter())||tDunningSmsTemplate.getNumafter()==null){
			tDunningSmsTemplate.setNumafter(9999);
		}
		
		   super.save(tDunningSmsTemplate);
//	       tcrService.updateTmisContatRwcord(tDunningSmsTemplate);
           
	}
    
	  //根据逾期天数对应的模板
	   public List<TmisDunningSmsTemplate> findSmsTemplate(String contactType,Date repaymentDate,TMisDunningOrder order,TMisDunningTask task){
		try{   
			  TmisDunningSmsTemplate template=new TmisDunningSmsTemplate();
			  
			  int overdayas =  (int) TMisDunningTaskService.GetOverdueDay(repaymentDate);
			  String acceptType=""; 
			  if("".equals(contactType)){
				  
				 acceptType=""; 
			  }else{
				  if("self".equalsIgnoreCase(contactType)){
					  acceptType="self";
				  }
				  else{
					  acceptType="others";
				  }
			  }
			  List<TmisDunningSmsTemplate> findList = tsTemplateDao.findListSMSTemplate(overdayas,acceptType);
			  
			 if(findList.size()!=0){
			  TmisDunningSmsTemplate tSmsTemplate=findList.get(0);
			  
			  String smsCotent = tSmsTemplate.getSmsCotent();
			  TMisDunningPeople tMisDunningPeople = tmisPeopleDao.get(task.getDunningpeopleid());
			  TRiskBuyerPersonalInfo buyerInfeo= tbuyerDao.getbuyerIfo(order.getDealcode());
			  String cousmscotent = this.cousmscotent(smsCotent,buyerInfeo,order.getPlatformExt(),task.getDunningpeopleid(),tMisDunningPeople.getExtensionNumber());
			  tSmsTemplate.setSmsCotent(cousmscotent); 
			 } 
			 return findList;
		}catch(Exception e){
			e.getMessage();
			logger.warn("根据逾期天数找对应的模板失败");
		}
		return null;
	}
	   
	 
		  /**
		   * 
		   * 填充短信内容
		   * @param smsCotent
		   * @return
		   */
        public String cousmscotent(String smsCotent,TRiskBuyerPersonalInfo buyerInfeo,String platformExt,String dunningpeopleid,String extensionNumber) {
        	
//        	TMisDunningPeople tMisDunningPeople = tmisPeopleDao.get(dunningpeopleid);
        	
//        	TRiskBuyerPersonalInfo buyerInfeo= tbuyerDao.getbuyerIfo(dealcode);
        	
        	SimpleDateFormat ss=new SimpleDateFormat("yyyy-MM-dd ");
        	
        	Map<String, Object> map = new HashMap<String, Object>();
        	
        	String  platform="";
        	String  creditAmount="";
        	
        	if(smsCotent.contains("${platform}")){
        		if(null!=platformExt&&!"".equals(platformExt)){
    	    		if(platformExt.contains("feishudai")){
    	    			platform ="飞鼠贷";
    	    		}else{
    	    			platform = "mo9";
    	    		}
        		}else{
        			platform = "mo9";

        		}
        	
        	smsCotent=smsCotent.replace("${platform}", platform);
        		
        	}
        	if(null!=buyerInfeo.getRealName()&&!"".equals(buyerInfeo.getRealName())){
	        	if(smsCotent.contains("${realName}")){
	        		smsCotent=smsCotent.replace("${realName}",buyerInfeo.getRealName());
	        	}
        	}
        	if(null!=buyerInfeo.getSex()&&!"".equals(buyerInfeo.getSex())){
	        	if(smsCotent.contains("${sex}")){
	        		if("男".equals(buyerInfeo.getSex())){
	        		smsCotent=smsCotent.replace("${sex}","先生");
	        		}
	        		if("女".equals(buyerInfeo.getSex())){
	        			smsCotent=smsCotent.replace("${sex}","女士");
	        		}
	        	}
        	}
        	if(null!=buyerInfeo.getIdcard()&&!"".equals(buyerInfeo.getIdcard())){
	        	if(smsCotent.contains("${idCard}")){
	        		String idcard = buyerInfeo.getIdcard();
	        		
	        		StringBuilder sb=new StringBuilder(idcard);
	        		sb= sb.replace(3, 15, "*********");
	        		
	        		smsCotent=smsCotent.replace("${idCard}",sb.toString());
	        	}
        	}
        	if(null!=buyerInfeo.getCreditAmount()&&!"".equals(buyerInfeo.getCreditAmount())){
	        	if(smsCotent.contains("${creditamount}")){
	        		creditAmount=String.valueOf((Double.valueOf(buyerInfeo.getCreditAmount()).doubleValue()/100D));
	        		smsCotent=smsCotent.replace("${creditamount}",creditAmount);
	        	}
        	}
        	if(null!=extensionNumber&&""!=extensionNumber){
	        	if(smsCotent.contains("${extensionNumber}")){
	        		smsCotent=smsCotent.replace("${extensionNumber}",extensionNumber);
	        	}
        	}
        	if(null!=buyerInfeo.getCreateTime()){
	        	if(smsCotent.contains("${creadateTime}")){
	        		
	        		smsCotent=smsCotent.replace("${creadateTime}",ss.format(buyerInfeo.getCreateTime()));
	        	}
        	}
        	if(null!=buyerInfeo.getRepaymentTime()){
	        	if(smsCotent.contains("${repaymentTime}")){
	        		smsCotent=smsCotent.replace("${repaymentTime}",ss.format(buyerInfeo.getRepaymentTime()));
	        	}
        	}
        	if(null!=buyerInfeo.getOverdueDays()){
	        	if(smsCotent.contains("${overduedays}")){
	        		smsCotent=smsCotent.replace("${overduedays}",buyerInfeo.getOverdueDays());
	        	}
        	}
        	if(smsCotent.contains("${todayDate}")){
        		smsCotent=smsCotent.replace("${todayDate}",ss.format(new Date()));
        	}
        	
        	return smsCotent;
        }	
        	
   
}
