package com.mo9.risk.modules.dunning.listener;

import com.alibaba.fastjson.JSON;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqAction;
import com.mo9.mqclient.MqMessage;
import com.mo9.risk.modules.dunning.bean.dto.Mo9MqMessage;
import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.entity.TRiskOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RiskOrderListener implements IMqMsgListener {

	private static final Logger logger = LoggerFactory.getLogger(RiskOrderListener.class);

	@Autowired
	TMisDunningOrderDao orderDao;

	@Override
	public MqAction consume(MqMessage msg, Object consumeContext) {
		logger.debug("消息队列接收到:");
		logger.info("Topic:"+msg.getTopic()+" Tag:"+msg.getTag());
		logger.info("Body:"+msg.getBody());
		logger.debug("Key:"+msg.getKey());
		logger.debug("MsgId:"+msg.getMsgId());
		String tag = msg.getTag();

		if ("mis_payoff".equals(tag)){
			return this.payoffSyn(msg);
		}
		return MqAction.ReconsumeLater;
	}

	/**
	 * @Description 还清订单同步
	 * @param msg
	 * @return com.mo9.mqclient.MqAction
	 */
	private MqAction payoffSyn(MqMessage msg) {
		String json = msg.getBody();
		Mo9MqMessage message = JSON.parseObject(json, Mo9MqMessage.class);
		String remark = message.getRemark();
		TRiskOrder order = JSON.parseObject(remark, TRiskOrder.class);

		String dealcode = order.getDealcode();
		if (orderDao.findOrderByDealcode(dealcode) != null ){
			orderDao.orderSynUpdate(order);
			return MqAction.CommitMessage;
		}
		//若订单不存在则不消费此消息
		return MqAction.ReconsumeLater;
	}
}
