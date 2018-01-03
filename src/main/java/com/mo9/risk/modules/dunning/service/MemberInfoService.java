package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.MemberInfoDao;
import com.mo9.risk.modules.dunning.entity.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by cyuan on 2018/1/2.
 */
@Service
@Transactional(readOnly = true)
public class MemberInfoService {
    @Autowired
    private MemberInfoDao memberInfoDao;
    public MemberInfo getMemberInfo(String mobile) {
        MemberInfo memberInfo = memberInfoDao.getMemberInfo(mobile);
        if(memberInfo == null){
            return null;
        }
        Integer numberOfTime = memberInfo.getNumberOfTime();
        long time = memberInfo.getOverdueTime().getTime();
        long time1 = new Date().getTime();
        if(time<time1){
            //说明过期了
            memberInfo.setRemark("超过有效期，借款次数已使用");
        }else{
            if(numberOfTime ==0){
            memberInfo.setRemark("会员有效期内，次数用完");
            }
        }
        return memberInfo;
    }
}
