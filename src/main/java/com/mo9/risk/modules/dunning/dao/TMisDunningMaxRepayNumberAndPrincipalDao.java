package com.mo9.risk.modules.dunning.dao;

import com.mo9.risk.modules.dunning.entity.DunningMaxRepayNumberAndPrincipal;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.Date;
import java.util.List;

/**
 * Created by jxguo on 2017/12/12.
 */
@MyBatisDao
public interface TMisDunningMaxRepayNumberAndPrincipalDao {

    public List<DunningMaxRepayNumberAndPrincipal> getPersonalMaxRepayNumberAndPrincipalListofDay(Date dateTime);

    public List<DunningMaxRepayNumberAndPrincipal> getGroupMaxRepayNumberAndPrincipalListofDay(Date dateTime);

    public List<DunningMaxRepayNumberAndPrincipal> getPersonalMaxRepayNumberAndPrincipalListofPeriod(Date begintime, Date endtime);

    public List<DunningMaxRepayNumberAndPrincipal> getGroupMaxRepayNumberAndPrincipalListofPeriod(Date begintime, Date endtime);

    public int countPersonalMaxRepayNumberAndPrincipalListofDay(Date dateTime);

    public int countGroupMaxRepayNumberAndPrincipalListofDay(Date dateTime);

    public int countPersonalMaxRepayNumberAndPrincipalListofPeriod(Date begintime, Date endtime);

    public int countGroupMaxRepayNumberAndPrincipalListofPeriod(Date begintime, Date endtime);
}
