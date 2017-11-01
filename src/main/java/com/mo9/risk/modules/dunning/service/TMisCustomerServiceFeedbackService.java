package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisCustomerServiceFeedbackDao;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客服问题service
 * Created by qtzhou on 2017/10/31.
 */
@Service
@Lazy(false)
@Transactional(readOnly = true)
public class TMisCustomerServiceFeedbackService extends CrudService<TMisCustomerServiceFeedbackDao,TMisCustomerServiceFeedback> {

    @Autowired
    private TMisCustomerServiceFeedbackDao tMisCustomerServiceFeedbackDao;

    /**
     *@Description 通过订单编号,类型,状态查询反馈案件
     * @param tMisCustomerServiceFeedback
     */
    public List<TMisCustomerServiceFeedback> findFeedbackByCodeTypeStatus(TMisCustomerServiceFeedback tMisCustomerServiceFeedback){

        return tMisCustomerServiceFeedbackDao.findFeedbackByCodeTypeStatus(tMisCustomerServiceFeedback);
    }

    /**
     * @Description 通过问题状态,推送标签,推送人查询反馈案件
     * @param problemstatus
     * @param hashtag
     * @param pushpeople
     */
    public List<TMisCustomerServiceFeedback> findFeedbackByStatusTagPushPeople(@Param("problemstatus")String problemstatus, @Param("hashtag")String hashtag, @Param("problemdescription")String problemdescription,@Param("pushpeople")String pushpeople) {

        if (problemstatus == null && hashtag == null&& problemdescription==null && pushpeople == null) {
            return null;
        }
        return tMisCustomerServiceFeedbackDao.findFeedbackByStatusTagProblemPeople(problemstatus,hashtag,problemdescription,pushpeople);
    }

    /**
     * @Description 通过推送标签查询一个反馈案件
     * @param problemstatus
     */
    public TMisCustomerServiceFeedback findFeedbackByTag(@Param("problemstatus")String problemstatus){

        if ( problemstatus == null) {
            return null;
        }
        return tMisCustomerServiceFeedbackDao.findFeedbackByStatus(problemstatus);
    }

    public Page<TMisCustomerServiceFeedback> findPage(Page<TMisCustomerServiceFeedback> page, TMisCustomerServiceFeedback tMisCustomerServiceFeedback) {
        // 设置排序参数
        page.setOrderBy("ProblemStatus DESC");
        return super.findPage(page, this.get(tMisCustomerServiceFeedback));
    }

}
