package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.DunningInformationRecovery;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecoveryHistoryRecord;
import com.mo9.risk.modules.dunning.entity.DunningInformationRecoveryLog;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * Created by jxguo on 2017/11/1.
 */
@MyBatisDao
public interface TMisDunningInformationRecoveryDao extends CrudDao<DunningInformationRecovery> {

    public int saveInformationRecovery(DunningInformationRecovery dunningInformationRecovery);

    public int saveInformationRecoveryLog(DunningInformationRecoveryLog DunningInformationRecoveryLog);

    public void updateInformationRecovery(DunningInformationRecovery dunningInformationRecovery);

    public List<DunningInformationRecovery> findInformationRecoveryList(DunningInformationRecovery dunningInformationRecovery);

    public int saveInformationRecoveryHistoryRecord(DunningInformationRecoveryHistoryRecord dunningInformationRecoveryHistoryRecord);

    public List<DunningInformationRecoveryHistoryRecord> findInformationRecoveryHistoryRecordList(DunningInformationRecoveryHistoryRecord dunningInformationRecoveryHistoryRecord);

    public void updateHistoryCount(DunningInformationRecovery dunningInformationRecovery);
}
