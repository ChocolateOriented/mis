package com.mo9.risk.modules.dunning.sendMessage.service;

import com.mo9.risk.modules.dunning.sendMessage.pojo.MessageEntity;
import com.mo9.risk.modules.dunning.sendMessage.dao.MessageEntityDao;
import com.thinkgem.jeesite.common.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by qtzhou on 2017/11/9.
 */
@Service
@Lazy(false)
@Transactional(readOnly = true)
public class MessageService extends CrudService<MessageEntityDao,MessageEntity> {

    @Autowired
    private  MessageEntityDao messageDao;


    public List<MessageEntity> getResendMessages() {
        return messageDao.getResendMessages();
    }

    public void saveMessage(MessageEntity message) {

    }

    public int updateMessage(MessageEntity message) {
        return messageDao.updateMessage(message);
    }

    public Date generateNextSendTime(Date nextSendTime, int times) {
        return messageDao.generateNextSendTime(nextSendTime,times);
    }
}
