package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.MisMqMessage;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import java.util.List;

/**
 * Created by jxli on 2017/11/10.
 */
@MyBatisDao
public interface MisMqMessageDao extends CrudDao<MisMqMessage> {

	/**
	 * @Description 查询需要重发的消息(MsgId为Null的消息)
	 * @param
	 * @return java.util.List<com.mo9.risk.modules.dunning.entity.MisMqMessage>
	 */
	List<MisMqMessage> findResendMessages();
}
