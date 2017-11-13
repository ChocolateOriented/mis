package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningInformationRecoveryDao;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecovery;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecoveryHistoryRecord;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecoveryLog;
import com.thinkgem.jeesite.common.service.CrudService;
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

    @Transactional(readOnly = false)
    public int saveInformationRecovery(DunningInformationRecovery dunningInformationRecovery){
        dunningInformationRecovery.preInsert();
        DunningInformationRecoveryLog dunningInformationRecoveryLog = new DunningInformationRecoveryLog();
        BeanUtils.copyProperties(dunningInformationRecovery,dunningInformationRecoveryLog);
        dunningInformationRecoveryLog.setOperationType("save");

        tMisDunningInformationRecoveryDao.saveInformationRecovery(dunningInformationRecovery);
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
