package com.mo9.risk.modules.dunning.dao;


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


}
