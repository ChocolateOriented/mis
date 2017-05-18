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

	/**
	 * 获取所有模板
	 * @param entity
	 * @return
	 */
	public List<TmisDunningSmsTemplate> findSmsPageList(TmisDunningSmsTemplate entity);
	/**
	 * 获取手工模板通过逾期天数和短信类型
	 * @param overdays
	 * @param acceptType
	 * @return
	 */
	public List<TmisDunningSmsTemplate> findListSMSTemplate(@Param(value="overdays")Integer overdays,@Param(value="acceptType")String acceptType);
    
	
	/**
	 * 通过模板名获取手工短信模板
	 * @param templateName
	 * @return
	 */
	public TmisDunningSmsTemplate getByName(String templateName);
	
	
	
	/**
	 * 获取所有系统的模板
	 * @return
	 */
	public List<TmisDunningSmsTemplate>  findByAutoSend();

	public TmisDunningSmsTemplate getByEnglishName(String englishTemplateName);
	

}
