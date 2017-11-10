package com.mo9.risk.modules.dunning.sendMessage.dao;

import com.mo9.risk.modules.dunning.sendMessage.pojo.MessageEntity;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.Date;
import java.util.List;

/**
 * Created by qtzhou on 2017/11/9.
 * 回传解决问题消息Dao
 */
@MyBatisDao
public interface MessageEntityDao extends CrudDao<MessageEntity> {

    public  List<MessageEntity> getResendMessages();

    public  void saveMessage(MessageEntity message);

    /**
     * 根据id更新消息，仅更新不为null字段
     */
    public  int updateMessage(MessageEntity message);

    public  Date generateNextSendTime(Date nextSendTime, int times);
}
