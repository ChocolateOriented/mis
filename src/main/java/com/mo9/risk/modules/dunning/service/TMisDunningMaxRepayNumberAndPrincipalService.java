package com.mo9.risk.modules.dunning.service;

import com.mo9.risk.modules.dunning.dao.TMisDunningMaxRepayNumberAndPrincipalDao;
import com.mo9.risk.modules.dunning.entity.DunningMaxRepayNumberAndPrincipal;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jxguo on 2017/12/12.
 */
@Service
@Transactional(readOnly = true)
public class TMisDunningMaxRepayNumberAndPrincipalService {

    @Autowired
    private TMisDunningMaxRepayNumberAndPrincipalDao dunningMaxRepayNumberAndPrincipalDao;

    public Page<DunningMaxRepayNumberAndPrincipal> getPersonalMaxRepayNumberAndPrincipalPageofDay(Page<DunningMaxRepayNumberAndPrincipal> page, DunningMaxRepayNumberAndPrincipal entity){
        entity.setPage(page);
        page.setUsePaginationInterceptor(false);
        page.setCount(dunningMaxRepayNumberAndPrincipalDao.countPersonalMaxRepayNumberAndPrincipalListofDay(entity.getDateTime()));
        List<DunningMaxRepayNumberAndPrincipal> list = dunningMaxRepayNumberAndPrincipalDao.getPersonalMaxRepayNumberAndPrincipalListofDay(entity.getDateTime());
        return page.setList(list);
    }

    public Page<DunningMaxRepayNumberAndPrincipal> getGroupMaxRepayNumberAndPrincipalPageofDay(Page<DunningMaxRepayNumberAndPrincipal> page, DunningMaxRepayNumberAndPrincipal entity){
        entity.setPage(page);
        page.setUsePaginationInterceptor(false);
        page.setCount(dunningMaxRepayNumberAndPrincipalDao.countGroupMaxRepayNumberAndPrincipalListofDay(entity.getDateTime()));
        List<DunningMaxRepayNumberAndPrincipal> list = dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofDay(entity.getDateTime());
        return page.setList(list);
    }

    public Page<DunningMaxRepayNumberAndPrincipal> getPersonalMaxRepayNumberAndPrincipalPageofPeriod(Page<DunningMaxRepayNumberAndPrincipal> page, DunningMaxRepayNumberAndPrincipal entity){
        entity.setPage(page);
        page.setUsePaginationInterceptor(false);
        getHalfMonthOrFullMonth(entity);
        List<DunningMaxRepayNumberAndPrincipal> list = dunningMaxRepayNumberAndPrincipalDao.getPersonalMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime());
        page.setCount(dunningMaxRepayNumberAndPrincipalDao.countPersonalMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime()));
        setHalfMonthOrFullMonth(list, entity);
        return page.setList(list);
    }

    public Page<DunningMaxRepayNumberAndPrincipal> getGroupMaxRepayNumberAndPrincipalPageofPeriod(Page<DunningMaxRepayNumberAndPrincipal> page, DunningMaxRepayNumberAndPrincipal entity){
        entity.setPage(page);
        page.setUsePaginationInterceptor(false);
        getHalfMonthOrFullMonth(entity);
        List<DunningMaxRepayNumberAndPrincipal> list = dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime());
        page.setCount(dunningMaxRepayNumberAndPrincipalDao.countGroupMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime()));
        setHalfMonthOrFullMonth(list, entity);
        return page.setList(list);
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportPersonalMaxRepayNumberAndPrincipalListofDay(DunningMaxRepayNumberAndPrincipal entity){
        return dunningMaxRepayNumberAndPrincipalDao.getPersonalMaxRepayNumberAndPrincipalListofDay(entity.getDateTime());
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportGroupMaxRepayNumberAndPrincipalListofDay(DunningMaxRepayNumberAndPrincipal entity){
        return dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofDay(entity.getDateTime());
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportPersonalMaxRepayNumberAndPrincipalListofPeriod(DunningMaxRepayNumberAndPrincipal entity){
        getHalfMonthOrFullMonth(entity);
        List<DunningMaxRepayNumberAndPrincipal> list = dunningMaxRepayNumberAndPrincipalDao.getPersonalMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime());
        setHalfMonthOrFullMonth(list, entity);
        return list;
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportGroupMaxRepayNumberAndPrincipalListofPeriod(DunningMaxRepayNumberAndPrincipal entity){
        getHalfMonthOrFullMonth(entity);
        List<DunningMaxRepayNumberAndPrincipal> list = dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime());
        setHalfMonthOrFullMonth(list, entity);
        return list;
    }

    private void getHalfMonthOrFullMonth(DunningMaxRepayNumberAndPrincipal entity){
        Date beginTime = null;
        Date endTime = null;
        String month = DateUtils.formatDate(entity.getDateTime(),"MM");
        List<String> bigMonthList = Arrays.asList("01","03","05","07","08","10","12");
        String dateTime =DateUtils.formatDate(entity.getDateTime(),"yyyy-MM");
        int intMonth = Integer.parseInt(DateUtils.formatDate(entity.getDateTime(),"MM"));
        if (bigMonthList.contains(month)){
            if (entity.getMonthdesc().equals("上半月")){
                beginTime = DateUtils.parseDate(dateTime+"-01");
                endTime = DateUtils.parseDate(dateTime+"-17");
            }else{
                beginTime = DateUtils.parseDate(dateTime+"-17");
                endTime = addMonth(entity.getDateTime(), 1);
            }
        }else{
            if (entity.getMonthdesc().equals("上半月")){
                beginTime = DateUtils.parseDate(dateTime+"-01");
                endTime = DateUtils.parseDate(dateTime+"-16");
            }else{
                beginTime = DateUtils.parseDate(dateTime+"-16");
                endTime = addMonth(entity.getDateTime(), 1);
            }
        }
        entity.setBegintime(beginTime);
        entity.setEndtime(endTime);
    }

    private void setHalfMonthOrFullMonth(List<DunningMaxRepayNumberAndPrincipal> list, DunningMaxRepayNumberAndPrincipal entity){
        for (DunningMaxRepayNumberAndPrincipal repayNumberAndPrincipal : list){
            repayNumberAndPrincipal.setDateTime(entity.getDateTime());
            repayNumberAndPrincipal.setMonthdesc(entity.getMonthdesc());
        }
    }

    private Date addMonth(Date date, int add){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+add);//让日期加1
        return calendar.getTime();
    }
}
