package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisCustomerServiceFeedbackDao;
import com.mo9.risk.modules.dunning.dao.TMisDunningInformationRecoveryDao;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecovery;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecoveryHistoryRecord;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecoveryLog;
import com.mo9.risk.modules.dunning.entity.TMisCustomerServiceFeedback;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jxguo on 2017/11/1.
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningInformationRecoveryService extends CrudService<TMisDunningInformationRecoveryDao,DunningInformationRecovery> {

    @Autowired
    private TMisDunningInformationRecoveryDao tMisDunningInformationRecoveryDao;
    @Autowired
    private TMisCustomerServiceFeedbackService tMisCustomerServiceFeedbackService;
    @Autowired
    private TMisCustomerServiceFeedbackDao tMisCustomerServiceFeedbackDao;
    @Transactional(readOnly = false)
    public int saveInformationRecovery(DunningInformationRecovery dunningInformationRecovery){
        dunningInformationRecovery.preInsert();
        DunningInformationRecoveryLog dunningInformationRecoveryLog = new DunningInformationRecoveryLog();
        BeanUtils.copyProperties(dunningInformationRecovery,dunningInformationRecoveryLog);
        dunningInformationRecoveryLog.setOperationType("save");

        tMisDunningInformationRecoveryDao.saveInformationRecovery(dunningInformationRecovery);
        try {
            //如果当信息修复新增号码，且号码与备注中的号码一致,更新通知
            User user = UserUtils.getUser();
            String name = tMisCustomerServiceFeedbackDao.getNameById(user.getId());;
            if(name == null){
                 name = user.getName();
            }

            String contactNumber = dunningInformationRecovery.getContactNumber();
            String dealcode = dunningInformationRecovery.getDealCode();
            TMisCustomerServiceFeedback tMisCustomerServiceFeedback = new TMisCustomerServiceFeedback();
            tMisCustomerServiceFeedback.setDealcode(dealcode);
            tMisCustomerServiceFeedback.setHandlingresult("已添加,");
            tMisCustomerServiceFeedback.setHashtag("CONTACT_REMARK");
            tMisCustomerServiceFeedback.setNickname(name);
            tMisCustomerServiceFeedback.setContactNumber(contactNumber);
            tMisCustomerServiceFeedbackDao.updateHandlingResult(tMisCustomerServiceFeedback);
            String ids = tMisCustomerServiceFeedback.getIds();
            if(ids != null){
                tMisCustomerServiceFeedbackService.changeProblemStatus(ids);
            }
        }catch (Exception e){
            logger.info("修改通知备注联系方式错误",e);
        }
        return tMisDunningInformationRecoveryDao.saveInformationRecoveryLog(dunningInformationRecoveryLog);
    }
    @Transactional(readOnly = false)
    public void updateInformationRecovery(DunningInformationRecovery dunningInformationRecovery){
        dunningInformationRecovery.preUpdate();
        DunningInformationRecoveryLog dunningInformationRecoveryLog = new DunningInformationRecoveryLog();
        BeanUtils.copyProperties(dunningInformationRecovery,dunningInformationRecoveryLog);
        dunningInformationRecoveryLog.setOperationType("update");
        dunningInformationRecoveryLog.setCreateBy(dunningInformationRecoveryLog.getUpdateBy());
        dunningInformationRecoveryLog.setCreateDate(dunningInformationRecoveryLog.getUpdateTime());
        tMisDunningInformationRecoveryDao.updateInformationRecovery(dunningInformationRecovery);
        tMisDunningInformationRecoveryDao.saveInformationRecoveryLog(dunningInformationRecoveryLog);

    }

    public List<DunningInformationRecovery> findInformationRecoveryList(DunningInformationRecovery dunningInformationRecovery){
        return tMisDunningInformationRecoveryDao.findInformationRecoveryList(dunningInformationRecovery);
    }

    @Transactional(readOnly = false)
    public int saveInformationRecoveryHistoryRecord(DunningInformationRecoveryHistoryRecord dunningInformationRecoveryHistoryRecord){
        dunningInformationRecoveryHistoryRecord.setIsNewRecord(true);
        dunningInformationRecoveryHistoryRecord.preInsert();
        DunningInformationRecovery dunningInformationRecovery = new DunningInformationRecovery();
        BeanUtils.copyProperties(dunningInformationRecoveryHistoryRecord,dunningInformationRecovery);
        tMisDunningInformationRecoveryDao.updateHistoryCount(dunningInformationRecovery);
        return tMisDunningInformationRecoveryDao.saveInformationRecoveryHistoryRecord(dunningInformationRecoveryHistoryRecord);
    }

    public List<DunningInformationRecoveryHistoryRecord> findInformationRecoveryHistoryRecordList(DunningInformationRecoveryHistoryRecord dunningInformationRecoveryHistoryRecord){
        return tMisDunningInformationRecoveryDao.findInformationRecoveryHistoryRecordList(dunningInformationRecoveryHistoryRecord);
    }


}
