package com.mo9.risk.modules.dunning.listener;

import com.alibaba.fastjson.JSON;
import com.mo9.mqclient.IMqMsgListener;
import com.mo9.mqclient.MqAction;
import com.mo9.mqclient.MqMessage;
import com.mo9.risk.modules.dunning.bean.dto.TMisCustomerServicefeedbackDto;
import com.mo9.risk.modules.dunning.entity.TaskIssue;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueChannel;
import com.mo9.risk.modules.dunning.entity.TaskIssue.IssueStatus;
import com.mo9.risk.modules.dunning.entity.TaskIssue.RemindingType;
import com.mo9.risk.modules.dunning.service.TaskIssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 客服后台数据接收
 * Created by qtzhou on 2017/10/30.
 */
@Service
public class CustomerServiceFeedbackListener implements IMqMsgListener {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceFeedbackListener.class);
    @Autowired
    TaskIssueService taskIssueService;

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
        logger.info(json);
        TMisCustomerServicefeedbackDto feedback = JSON.parseObject(json, TMisCustomerServicefeedbackDto.class );
        TaskIssue taskIssue = new TaskIssue();
        taskIssue.setStatus(IssueStatus.UNRESOLVED);
        taskIssue.setIssueChannel(IssueChannel.CUSTOMER_SERVICE);
        taskIssue.setRemindingType(RemindingType.DUNNING_PEOPLE);
        //使用客服反馈的ID,以便于回调
        taskIssue.setId(feedback.getFeedbackRecordId());
        taskIssue.setDealcode(feedback.getLoanDealCode());
        taskIssue.setDescription(feedback.getDescription());
        taskIssue.setIssueTypesByJson(feedback.getLabels());
        taskIssue.setRecorderName(feedback.getRecorderName());
        taskIssue.setUserName(feedback.getUserName());
        taskIssue.setCreateDate(feedback.getEventId());
        taskIssue.setUpdateRole("客服");

        taskIssueService.save(taskIssue);
        return MqAction.CommitMessage;
    }
}
