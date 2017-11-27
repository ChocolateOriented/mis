package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客服问题Dao接口
 * Created by qtzhou on 2017/10/26.
 */
@MyBatisDao
public interface TMisCustomerServiceFeedbackDao extends CrudDao<TMisCustomerServiceFeedback>{


    /**
     * @Description 客服推送消息的订单号,标签,时间的通知列表分页展示
     * @param tMisCustomerServiceFeedback
     */
    public List<TMisCustomerServiceFeedback> NotifyList(TMisCustomerServiceFeedback tMisCustomerServiceFeedback);

    /**
     * 更新问题状态,待解决操作
     * @param tMisCustomerServiceFeedback
     */
    public int updateFeedback(TMisCustomerServiceFeedback tMisCustomerServiceFeedback);

    /**
     * 获取订单编号,状态，反馈标签,描述,推送人
     * @param customerServiceFeedback
     * @return
     */
    public TMisCustomerServiceFeedback findCodeStatusTagDesPeople(TMisCustomerServiceFeedback customerServiceFeedback);

    /**
     * 获取客服通知数目
     * @param tMisCustomerServiceFeedback
     */
    public Integer findCustServiceCount(TMisCustomerServiceFeedback tMisCustomerServiceFeedback);
}
