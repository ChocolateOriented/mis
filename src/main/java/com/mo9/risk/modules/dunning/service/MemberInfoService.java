package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.MemberInfoDao;
import com.mo9.risk.modules.dunning.entity.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cyuan on 2018/1/2.
 */
@Service
@Transactional(readOnly = true)
public class MemberInfoService {
    @Autowired
    private MemberInfoDao memberInfoDao;
    public MemberInfo getMemberInfo(String dealcode) {
        MemberInfo memberInfo = memberInfoDao.getMemberInfo(dealcode);
        if(memberInfo == null){
            return null;
        }
        //有效期
        String memberType = memberInfo.getMemberType();
        String validDate = "bronze".equals(memberType.split("_")[1])? "7" :"30";
        memberInfo.setValidDate(validDate);
        //到期时间
        int i = Integer.parseInt(validDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(memberInfo.getStartTime());
        calendar.add(calendar.DATE,i);
        memberInfo.setOverdueTime(calendar.getTime());
        //会员卡状态
        Calendar now= Calendar.getInstance();
        now.setTime(new Date());
        if(now.compareTo(calendar)== -1){
            //说明未过期
            if("bronze".equals(memberType.split("_")[1])){
                //说明是单次卡
                memberInfo.setUseTypeText("已失效");
                memberInfo.setRemark("会员有效期内，次数用完");
            }else if("silver".equals(memberType.split("_")[1])){
                //说明是双次卡
                MemberInfo memberInfo2 = memberInfoDao.getNumberOfTime(memberInfo.getLogId());

                if(memberInfo2.getNumberOfTime()<2){
                    //说明次数没有用完
                    memberInfo.setUseTypeText("使用中");
                    memberInfo.setRemark("");
                }else {
                    //说明次数用完
                    memberInfo.setUseTypeText("已失效");
                    memberInfo.setRemark("会员有效期内，次数用完");
                }
            }else {
                //说明是月卡
                memberInfo.setUseTypeText("使用中");
                memberInfo.setRemark("");
            }
//            MemberInfo memberInfo2 = memberInfoDao.getNumberOfTime(memberInfo.getMobile());
//            String useType2 = memberInfo2.getUseType();
//            if("used".equals(useType2)){
//                //说明已经用完
//                memberInfo.setUseTypeText("已失效");
//                memberInfo.setRemark("会员有效期内，次数用完");
//            }else if("overdue".equals(useType2)){
//                //说明已过期
//                memberInfo.setUseTypeText("已失效");
//                memberInfo.setRemark("超过有效期，借款次数已使用");
//            }else {
//                //说明使用中
//                memberInfo.setUseTypeText("使用中");
//                memberInfo.setRemark("");
//            }
        }else{
            //说明已过期
            memberInfo.setUseTypeText("已失效");
            memberInfo.setRemark("超过有效期，借款次数已使用");
        }

        return memberInfo;
    }
}
