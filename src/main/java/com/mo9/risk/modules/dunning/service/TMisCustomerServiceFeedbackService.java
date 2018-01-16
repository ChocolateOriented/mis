package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisCustomerServiceFeedbackDao;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.db.DynamicDataSource;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.mo9.risk.modules.dunning.service.TMisDunningTaskService.*;

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

    @Autowired
    TMisDunningGroupService tMisDunningGroupService;

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
    public Page<TMisCustomerServiceFeedback> notifyList(Page<TMisCustomerServiceFeedback> page, TMisCustomerServiceFeedback tMisCustomerServiceFeedback){

        int permissions = getPermissions();
        List<String> allowedGroupIds = new ArrayList<String>();
        if(DUNNING_COMMISSIONER_PERMISSIONS == permissions){
            tMisCustomerServiceFeedback.setDunningpeopleid(UserUtils.getUser().getId());
        }
        if (DUNNING_INNER_PERMISSIONS == permissions) {
            tMisCustomerServiceFeedback.setDunningpeopleid(null);
            allowedGroupIds.addAll(tMisDunningGroupService.findIdsByLeader(UserUtils.getUser()));
            tMisCustomerServiceFeedback.setGroupIds(allowedGroupIds);
        }
        if(DUNNING_OUTER_PERMISSIONS == permissions){
            tMisCustomerServiceFeedback.setDunningpeopleid(null);
        }
        if (DUNNING_SUPERVISOR == permissions) {
            TMisDunningGroup group = new TMisDunningGroup();
            group.setSupervisor(UserUtils.getUser());
            List<String> groupIds = tMisDunningGroupService.findSupervisorGroupList(group);
            allowedGroupIds.addAll(groupIds);
            tMisCustomerServiceFeedback.setGroupIds(allowedGroupIds);
        }
        if(DUNNING_ALL_PERMISSIONS == permissions){
            tMisCustomerServiceFeedback.setDunningpeopleid(null);
        }
        tMisCustomerServiceFeedback.setPage(page);
        page.setOrderBy("readFlag ASC,pushTime DESC");
        page.setList(dao.notifyList(tMisCustomerServiceFeedback));
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

    /**
     * 获取客服通知数目
     * @param tMisCustomerServiceFeedback
     */
    public int findCustServiceCount(TMisCustomerServiceFeedback tMisCustomerServiceFeedback){

        return tMisCustomerServiceFeedbackDao.findCustServiceCount(tMisCustomerServiceFeedback);
    }

    /**
     * 判断是否需要改变状态
     */
    @Transactional(readOnly = false)
    public  void changeProblemStatus(String id){
        TMisCustomerServiceFeedback tMisCustomerServiceFeedback = new TMisCustomerServiceFeedback();
        tMisCustomerServiceFeedback.setId(id);
        TMisCustomerServiceFeedback codeStatusTagDesPeople = this.findCodeStatusTagDesPeople(tMisCustomerServiceFeedback);
        String hashtag = codeStatusTagDesPeople.getHashtag();
        String handlingresult = codeStatusTagDesPeople.getHandlingresult();
        Boolean flag=true;
        if(!hashtag.contains("COMPLAIN_SHAKE") && !hashtag.contains("CONSULT_REPAY")){
            //备注联系方式
            if(hashtag.contains("CONTACT_REMARK")){
                //说明包含备注联系方式,判断是否有
                if(!handlingresult.contains("已添加,")){
                    flag=false;
                }
            }
            //判断订单代扣
            if(hashtag.contains("ORDER_DEDUCT")){
                //说明包含订单代扣,判断是否有
                if(!handlingresult.contains("代扣")||handlingresult.contains("无")){
                    flag=false;
                }
            }
                //催销账
            if(hashtag.contains("WRITE_OFF")){
                //说明包含订单代扣,判断是否有
                if(!handlingresult.contains("订单已还清,")){
                    flag=false;
                }
            }

            if(flag){
                tMisCustomerServiceFeedbackDao.updateProblemStatus(tMisCustomerServiceFeedback);
            }

        }
    }
}
