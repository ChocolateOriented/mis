package com.mo9.risk.modules.dunning.service;

import com.mo9.mqclient.IMqProducer;
import com.mo9.mqclient.MqMessage;
import com.mo9.mqclient.MqSendResult;
import com.mo9.risk.modules.dunning.dao.MisMqMessageDao;
import com.mo9.risk.modules.dunning.entity.MisMqMessage;
import com.thinkgem.jeesite.common.service.BaseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jxli on 2017/11/10.
 * 消息队列服务
 */
//@Service
@Lazy(false)
@Transactional(readOnly = true)
public class MisMqMessageService extends BaseService {
	@Autowired
	IMqProducer mqProducer;
	@Autowired
	MisMqMessageDao dao;

	/**
	 * @Description 发送消息到消息队列, 并持久化到数据库, 若是失败, 定时重发
	 * @param topic 消息主题
	 * @param tag 消息标签(主题下的分类)
	 * @param key 业务主键
	 * @param body 消息体
	 * @return void
	 */
	@Transactional
	public void send (String topic, String tag, String key, String body) {
		MisMqMessage misMqMessage = new MisMqMessage(topic, tag, key, body);
		dao.insert(misMqMessage);
		this.sendMqMessage(misMqMessage);
	}

	/**
	 * @Description 定时重试推送消息
	 * @param
	 * @return void
	 */
	@Scheduled(cron = "0 0/10 * * * ?")
	@Transactional
	public void messageResendTask () {
		List<MisMqMessage> messages= dao.findResendMessages();
		logger.info("正在重发消息, 共"+messages.size()+"条");
		for (MisMqMessage misMqMessage: messages) {
			this.sendMqMessage(misMqMessage);
		}
	}

	/**
	 * @Description 推送消息
	 * @param misMqMessage
	 * @return void
	 */
	@Transactional
	private void sendMqMessage(MisMqMessage misMqMessage) {
		MqMessage message = new MqMessage(misMqMessage.getTopic(), misMqMessage.getTag(), misMqMessage.getKey(), misMqMessage.getBody());
		try {
			MqSendResult result = mqProducer.send(message);
			misMqMessage.setMsgId(result.getMessageId());
			dao.update(misMqMessage);
		} catch (Exception e) {//消息发送失败
			logger.info("消息发送失败,等待重发" + misMqMessage.toString(), e);
		}
	}

}
