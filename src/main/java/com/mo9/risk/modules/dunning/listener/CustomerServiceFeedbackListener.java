package com.mo9.risk.modules.dunning.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqAction;
import com.mo9.mqclient.MqMessage;
import com.mo9.risk.modules.dunning.bean.dto.Mo9MqMessage;
import com.mo9.risk.modules.dunning.bean.dto.TMisCustomerServicefeedbackDto;
import com.mo9.risk.modules.dunning.dao.TMisCustomerServiceFeedbackDao;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.thinkgem.jeesite.common.persistence.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 客服后台数据接收
 * Created by qtzhou on 2017/10/30.
 */
public class CustomerServiceFeedbackListener implements IMqMsgListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceFeedbackListener.class);

    @Autowired
    TMisCustomerServiceFeedbackDao feedbackDao;

    @Override
    public MqAction consume(MqMessage msg, Object consumeContext) {

        String tag1=msg.getTag();
        if ("customerServiceFeedback_problem".equals(tag1)){
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
        TMisCustomerServicefeedbackDto feedback = JSON.parseObject(json, TMisCustomerServicefeedbackDto.class );
        TMisCustomerServiceFeedback tMisCustomerServiceFeedback = new TMisCustomerServiceFeedback();
        tMisCustomerServiceFeedback.setDealcode(feedback.getLoanDealCode());
        tMisCustomerServiceFeedback.setType(feedback.getLoanOrderType());
        tMisCustomerServiceFeedback.setStatus(feedback.getLoanStatus());
        tMisCustomerServiceFeedback.setProblemdescription(feedback.getDescription());
        tMisCustomerServiceFeedback.setCreateBy(tMisCustomerServiceFeedback.getCreateBy());
        tMisCustomerServiceFeedback.setId(feedback.getFeedbackRecordId());
        tMisCustomerServiceFeedback.setProblemstatus(feedback.getFeedbackStatus());
        tMisCustomerServiceFeedback.setHashtag(feedback.getLabels());
        tMisCustomerServiceFeedback.setPushpeople(feedback.getRecorderName());
        tMisCustomerServiceFeedback.setOperate(tMisCustomerServiceFeedback.getOperate());
        tMisCustomerServiceFeedback.setHandlingresult(tMisCustomerServiceFeedback.getHandlingresult());
        tMisCustomerServiceFeedback.setUname(feedback.getUserName());
        tMisCustomerServiceFeedback.setPushTime(feedback.getEventId());
        logger.debug(tMisCustomerServiceFeedback.getId());
        if(feedbackDao.get(tMisCustomerServiceFeedback)==null){
            feedbackDao.insert(tMisCustomerServiceFeedback);
        } else{
            feedbackDao.updateFeedback(tMisCustomerServiceFeedback);
        }
        return MqAction.CommitMessage;
    }

}
