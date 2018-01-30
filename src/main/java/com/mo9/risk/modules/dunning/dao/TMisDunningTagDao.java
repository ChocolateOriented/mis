/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mo9.risk.modules.dunning.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

import com.mo9.risk.modules.dunning.entity.TMisDunningTag;

/**
 * 用户标签DAO接口
 * @author shijlu
 * @version 2017-08-22
 */
@MyBatisDao
public interface TMisDunningTagDao extends CrudDao<TMisDunningTag> {
	
	/**
	 * 保存用户标签历史
	 * @param tMisDunningTag
	 * @return
	 */
	public int saveTagHistory(TMisDunningTag tMisDunningTag);

	/**
	 * 查询已存在的标签
	 * @param buyerid
	 * @return
	 */

	public List<String> getExistTagByBuyerid(String buyerid);

	public String typeExist(TMisDunningTag tMisDunningTag);

    public int upadateDelflag(TMisDunningTag tMisDunningTag);

	public List<TMisDunningTag> select(TMisDunningTag tMisDunningTag);
}