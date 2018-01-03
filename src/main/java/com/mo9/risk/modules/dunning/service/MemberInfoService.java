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
        calendar.add(calendar.DATE,30);
        memberInfo.setOverdueTime(calendar.getTime());
//        long time = i * 24*60*60*1000;
//        long l = memberInfo.getStartTime().getTime() + time;
//        memberInfo.setOverdueTime(date=calendar.getTime());
        //会员卡状态
        Calendar now= Calendar.getInstance();
        now.setTime(new Date());
        if(now.compareTo(calendar)== -1){
            //说明未过期
            MemberInfo memberInfo2 = memberInfoDao.getNumberOfTime(memberInfo.getMobile());
            String useType2 = memberInfo2.getUseType();
            if("used".equals(useType2)){
                //说明已经用完
                memberInfo.setUseTypeText("已失效");
                memberInfo.setRemark("会员有效期内，次数用完");
            }else if("overdue".equals(useType2)){
                //说明已过期
                memberInfo.setUseTypeText("已失效");
                memberInfo.setRemark("超过有效期，借款次数已使用");
            }else {
                //说明使用中
                memberInfo.setUseTypeText("使用中");
                memberInfo.setRemark("");
            }
        }else{
            //说明已过期
            memberInfo.setUseTypeText("已失效");
            memberInfo.setRemark("超过有效期，借款次数已使用");
        }

//        String useType = memberInfo.getUseType();
//        if("used".equals(useType)){
//            //说明已用完
//            memberInfo.setRemark("会员有效期内，次数用完");
//        }else if("overdue".equals(useType)){
//            //说明已过期
//            memberInfo.setRemark("超过有效期，借款次数已使用");
//        }else {
//            //说明使用中
//            memberInfo.setRemark("");
//        }
//        Integer numberOfTime = memberInfo.getNumberOfTime();
//        long time = memberInfo.getOverdueTime().getTime();
//        long time1 = new Date().getTime();
//        if(time<time1){
//            //说明过期了
//            memberInfo.setRemark("超过有效期，借款次数已使用");
//        }else{
//            if(numberOfTime ==0){
//            memberInfo.setRemark("会员有效期内，次数用完");
//            }
//        }
        return memberInfo;
    }
}
