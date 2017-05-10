package com.mo9.risk.modules.dunning.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.entity.TmisDunningSmsTemplate;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 短信接口dao
 * @author jwchi
 *
 */

@MyBatisDao
public interface TmisDunningSmsTemplateDao  extends CrudDao<TmisDunningSmsTemplate> {


	public List<TmisDunningSmsTemplate> findSmsPageList(TmisDunningSmsTemplate entity);
	
	public List<TmisDunningSmsTemplate> findListSMSTemplate(@Param(value="overdays")Integer overdays,@Param(value="acceptType")String acceptType);
    
	//通过模板名获取模板
	public TmisDunningSmsTemplate getByName(String templateName);
	
	
	//获取所有系统的模板
	public List<TmisDunningSmsTemplate>  findByAutoSend();
	

}
