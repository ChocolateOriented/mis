package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisCustomerServiceFeedbackDao;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
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
     * 客服推送的消息分页展示
     * @param page
     * @param tMisCustomerServiceFeedback
     * @return
     */
    public Page<TMisCustomerServiceFeedback> feedbackList(Page<TMisCustomerServiceFeedback> page, TMisCustomerServiceFeedback tMisCustomerServiceFeedback) {
        // 设置排序参数

        tMisCustomerServiceFeedback.setPage(page);
        page.setOrderBy("problemStatus DESC");
        page.setList(dao.findList(tMisCustomerServiceFeedback));

        return page;
    }

    /**
     * @Description 客服推送消息的订单号,标签,时间的通知列表分页展示
     * @param page
     */
    public Page<TMisCustomerServiceFeedback> NotifyList(Page<TMisCustomerServiceFeedback> page,TMisCustomerServiceFeedback tMisCustomerServiceFeedback){

        tMisCustomerServiceFeedback.setPage(page);
        page.setOrderBy("createTime DESC");
        page.setList(dao.NotifyList(tMisCustomerServiceFeedback));
        return page;
    }

    /**
     * 更新问题状态,待解决操作
     * @param tMisCustomerServiceFeedback
     */
    @Transactional(readOnly = false)
    public int updateFeedback(TMisCustomerServiceFeedback tMisCustomerServiceFeedback){
              tMisCustomerServiceFeedback.preUpdate();
        return tMisCustomerServiceFeedbackDao.updateFeedback(tMisCustomerServiceFeedback);
    }

    /**
     * 根据问题ID获取订单号,问题状态,标签,问题描述,推送人
     * @param
     */
    public TMisCustomerServiceFeedback findCodeStatusTagDesPeople(TMisCustomerServiceFeedback customerServiceFeedback){

        return tMisCustomerServiceFeedbackDao.findCodeStatusTagDesPeople(customerServiceFeedback);
    }
}
