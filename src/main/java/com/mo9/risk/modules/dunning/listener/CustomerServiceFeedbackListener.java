package com.mo9.risk.modules.dunning.listener;

import com.alibaba.fastjson.JSON;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqAction;
import com.mo9.mqclient.MqMessage;
import com.mo9.risk.modules.dunning.bean.dto.Mo9MqMessage;
import com.mo9.risk.modules.dunning.dao.TMisCustomerServiceFeedbackDao;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.thinkgem.jeesite.common.persistence.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 客服后台数据接收
 * Created by qtzhou on 2017/10/30.
 */
public class CustomerServiceFeedbackListener implements IMqMsgListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceFeedbackListener.class);

    @Autowired
    TMisCustomerServiceFeedbackDao customerDao;

    @Override
    public MqAction consume(MqMessage msg, Object consumeContext) {

        String tag1 = msg.getTag();
        if ("customerServiceFeedback_problemstatus".equals(tag1)){
            return this.customerServiceFeedback_problemstatus(msg);
        }
        String tag2=msg.getTag();
        if ("customerServiceFeedback_problem".equals(tag2)){
            return this.customerServiceFeedback_problem(msg);
        }
        return MqAction.ReconsumeLater;
    }

    /**
     * @Description 接收到客服后台的推送消息
     * @param msg
     * @return com.mo9.mqclient.MqAction
     */
    private MqAction customerServiceFeedback_problem(MqMessage msg) {

        String json = msg.getBody();
        Mo9MqMessage message = JSON.parseObject(json, Mo9MqMessage.class);
        String remark=message.getRemark();
        TMisCustomerServiceFeedback customer = JSON.parseObject(remark, TMisCustomerServiceFeedback.class);

        String problemstatus=customer.getProblemstatus();
        String hashtag=customer.getHashTag();
        String problemdescription=customer.getProblemdescriotion();
        String pushpeople=customer.getPushpeople();
        if(customerDao.findFeedbackByStatusTagProblemPeople(problemstatus,hashtag,problemdescription,pushpeople)!=null){
            customerDao.findList(customer);
            return MqAction.CommitMessage;
        }
        return MqAction.ReconsumeLater;
    }


    /**
     * @Description 未解决问题推送过来时
     * @param msg
     * @return com.mo9.mqclient.MqAction
     */
    private MqAction customerServiceFeedback_problemstatus(MqMessage msg){

        String json = msg.getBody();
        Mo9MqMessage message = JSON.parseObject(json, Mo9MqMessage.class);
        String remark=message.getRemark();
        TMisCustomerServiceFeedback customer = JSON.parseObject(remark, TMisCustomerServiceFeedback.class);

        String problemstatus=customer.getProblemstatus();
        if(customerDao.findFeedbackByStatus(problemstatus) !=null){
            customerDao.findList(customer);
            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;
    }

}
