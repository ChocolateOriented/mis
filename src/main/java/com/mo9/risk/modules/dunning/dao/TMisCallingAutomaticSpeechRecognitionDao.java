package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.TMisCallingQualityTest;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * Created by jxguo on 2018/1/2.
 */
@MyBatisDao
public interface TMisCallingAutomaticSpeechRecognitionDao extends CrudDao<TMisCallingQualityTest> {

    public int listCount(TMisCallingQualityTest entity);

    public List<TMisCallingQualityTest> findCallingRecordList(TMisCallingQualityTest entity);

    public String findCallingContentById (TMisCallingQualityTest entity);

}
