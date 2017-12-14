package com.mo9.risk.modules.dunning.dao;


import java.util.List;

import com.mo9.risk.modules.dunning.bean.TMisDunningLetterDownLoad;
import com.mo9.risk.modules.dunning.entity.TMisAgentInfo;
import com.mo9.risk.modules.dunning.entity.TMisDunningLetter;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 短信接口dao
 * @author jwchi
 *
 */

@MyBatisDao
public interface TMisDunningLetterDao  extends CrudDao<TMisDunningLetter> {
	public List<TMisAgentInfo> findList(TMisAgentInfo entity);
	/**
	 * 同步逾期60+案件
	 * @return
	 */
	public List<TMisDunningLetter> findSynDealcode();
	/**
	 * 批量保存信函
	 * @param synDealcodeList
	 */
	public void saveList(List<TMisDunningLetter> synDealcodeList);
	/**
	 * 批量保存信函日志
	 * @param synDealcodeList
	 */
	public void saveLogList(List<TMisDunningLetter> synDealcodeList);
	
	/**
	 * 更新返回信函状态
	 * @param list
	 */
	public void updateStatus(TMisDunningLetter tMisDunningLetter);
	/**
	 * 发送信函邮件的订单
	 * @param sendLetterDealcodes
	 * @param message
	 * @return
	 */
	public List<TMisDunningLetter> findSendList(List<String> sendLetterDealcodes);
	/**
	 * 下载要寄邮件的信函
	 * @param identity
	 * @return
	 */
	public List<TMisDunningLetterDownLoad> lettersDownLoad(String identity);

	
}
