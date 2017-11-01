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
     * 通过订单编号,类型,状态查询反馈案件
     * @param
     */
    public List<TMisCustomerServiceFeedback> findFeedbackByCodeTypeStatus(TMisCustomerServiceFeedback tMisCustomerServiceFeedback);

    /**
     * 通过问题状态,推送标签,推送人查询反馈案件
     * @param
     */
    public List<TMisCustomerServiceFeedback> findFeedbackByStatusTagProblemPeople(@Param("problemstatus")String problemstatus,@Param("hashtag")String hashtag,@Param("problemdescription")String problemdescription,@Param("pushpeople")String pushpeople);

    /**
     * 通过推送标签查询一个反馈案件
     * @param
     */
    public TMisCustomerServiceFeedback findFeedbackByStatus(@Param("problemstatus")String problemstatus);
}
