package com.mo9.risk.modules.dunning.listener;

import com.alibaba.fastjson.JSON;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqAction;
import com.mo9.mqclient.MqMessage;
import com.mo9.risk.modules.dunning.bean.dto.Mo9MqMessage;
import com.mo9.risk.modules.dunning.dao.TMisCustomerServiceFeedbackDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningOrderDao;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.mo9.risk.modules.dunning.entity.TRiskOrder;
import java.util.Date;

import com.mo9.risk.modules.dunning.service.TMisCustomerServiceFeedbackService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class RiskOrderListener implements IMqMsgListener {

	private static final Logger logger = LoggerFactory.getLogger(RiskOrderListener.class);

	@Autowired
	TMisDunningOrderDao orderDao;

	@Autowired
	private TMisCustomerServiceFeedbackDao tMisCustomerServiceFeedbackDao;

	@Autowired
	private TMisCustomerServiceFeedbackService tMisCustomerServiceFeedbackService;

	@Override
	public MqAction consume(MqMessage msg, Object consumeContext) {
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
			order.setUpdateTime(new Date());
			orderDao.orderSynUpdate(order);
			try{
			    //消息队列过来,说明订单已还清,同步通知表格
				TMisCustomerServiceFeedback tf = tMisCustomerServiceFeedbackDao.findNickNameByDealcode(dealcode);
                String nickname = null;
                if(tf != null){
                    nickname = tf.getNickname();
                }

                TMisCustomerServiceFeedback tMisCustomerServiceFeedback = new TMisCustomerServiceFeedback();
				tMisCustomerServiceFeedback.setDealcode(dealcode);
				tMisCustomerServiceFeedback.setHandlingresult("订单已还清,");
				tMisCustomerServiceFeedback.setHashtag("WRITE_OFF");
				tMisCustomerServiceFeedback.setNickname(nickname);
				while (true){
                    tMisCustomerServiceFeedbackDao.updateHandlingResult(tMisCustomerServiceFeedback);
                    String ids = tMisCustomerServiceFeedback.getIds();
                    if(ids==null){
                        break;
                    }
                    tMisCustomerServiceFeedbackService.changeProblemStatus(ids);
                }


			}catch (Exception e){
				logger.info("消息队列过来数据订单已经还清,更新通知状态错误",e);
			}
			return MqAction.CommitMessage;
		}
		//若订单不存在则不消费此消息
		return MqAction.ReconsumeLater;
	}
}
