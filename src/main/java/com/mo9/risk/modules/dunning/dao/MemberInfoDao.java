package com.mo9.risk.modules.dunning.dao;


import com.mo9.risk.modules.dunning.entity.MemberInfo;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * Created by cyuan on 2017/12/28.
 */
@MyBatisDao
public interface MemberInfoDao {
    MemberInfo getMemberInfo(String dealcode);

    MemberInfo getNumberOfTime(String mobile);
}
