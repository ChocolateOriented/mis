package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import java.util.List;

/**
 * 客服问题Dao接口
 * Created by qtzhou on 2017/10/26.
 */
@MyBatisDao
public interface TMisCustomerServiceFeedbackDao extends CrudDao<TMisCustomerServiceFeedback>{

    /**
     * 查询所有问题反馈的案件
     * @param 
     * return
     */
    public List<TMisCustomerServiceFeedback> findList(TMisCustomerServiceFeedback tMisCustomerServiceFeedback);



}
