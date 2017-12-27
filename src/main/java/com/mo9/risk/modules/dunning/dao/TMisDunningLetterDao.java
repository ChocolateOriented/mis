package com.mo9.risk.modules.dunning.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mo9.risk.modules.dunning.bean.TMisDunningLetterDownLoad;
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
	/**
	 * 同步逾期历史60+(可配置)案件
	 * @param daySyn 
	 * @return
	 */
	public List<TMisDunningLetter> findSynDealcode(@Param("daySyn")Integer daySyn,@Param("historyDays")String historyDays);
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
	/**
	 * 通过每次发送邮件的标识找对应要发送的信函订单量
	 * @param uuid
	 * @return
	 */
	public int findcountLetter(String uuid);
	/**
	 * 通过订单获取信函
	 * @param dealcode
	 * @return
	 */
	public TMisDunningLetter findLetterByDealcode(String dealcode);
	

	
}
