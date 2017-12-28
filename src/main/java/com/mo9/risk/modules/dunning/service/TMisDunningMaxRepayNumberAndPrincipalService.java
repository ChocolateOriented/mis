package com.mo9.risk.modules.dunning.service;

import com.alibaba.druid.util.StringUtils;
import com.mo9.risk.modules.dunning.dao.TMisDunningMaxRepayNumberAndPrincipalDao;
import com.mo9.risk.modules.dunning.entity.DunningMaxRepayNumberAndPrincipal;
import com.mo9.risk.modules.dunning.entity.TMisDunningGroup;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

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
        List<DunningMaxRepayNumberAndPrincipal> list = groupMaxRepayperNumberAndperPrincipalList(dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofDay(entity.getDateTime()));
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
        List<DunningMaxRepayNumberAndPrincipal> list = groupMaxRepayperNumberAndperPrincipalList(dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime()));
        page.setCount(dunningMaxRepayNumberAndPrincipalDao.countGroupMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime()));
        setHalfMonthOrFullMonth(list, entity);
        return page.setList(list);
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportPersonalMaxRepayNumberAndPrincipalListofDay(DunningMaxRepayNumberAndPrincipal entity){
        return dunningMaxRepayNumberAndPrincipalDao.getPersonalMaxRepayNumberAndPrincipalListofDay(entity.getDateTime());
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportGroupMaxRepayNumberAndPrincipalListofDay(DunningMaxRepayNumberAndPrincipal entity){
        return groupMaxRepayperNumberAndperPrincipalList(dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofDay(entity.getDateTime()));
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportPersonalMaxRepayNumberAndPrincipalListofPeriod(DunningMaxRepayNumberAndPrincipal entity){
        getHalfMonthOrFullMonth(entity);
        List<DunningMaxRepayNumberAndPrincipal> list = dunningMaxRepayNumberAndPrincipalDao.getPersonalMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime());
        setHalfMonthOrFullMonth(list, entity);
        return list;
    }

    public List<DunningMaxRepayNumberAndPrincipal> exportGroupMaxRepayNumberAndPrincipalListofPeriod(DunningMaxRepayNumberAndPrincipal entity){
        getHalfMonthOrFullMonth(entity);
        List<DunningMaxRepayNumberAndPrincipal> list = groupMaxRepayperNumberAndperPrincipalList(dunningMaxRepayNumberAndPrincipalDao.getGroupMaxRepayNumberAndPrincipalListofPeriod(entity.getBegintime(), entity.getEndtime()));
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

    /**
     * 计算每组多少人
     * key：组名
     * value：人数
     * @return
     */
    private Map<String, Integer> countPeopleOfGroup(){
        List<TMisDunningGroup> list = dunningMaxRepayNumberAndPrincipalDao.countPeopleOfGroup();
        Map<String, Integer> map = new HashMap<String, Integer>(list.size());
        for (TMisDunningGroup group : list){
            if (!StringUtils.isEmpty(group.getName())){
                map.put(group.getName(), group.getNumberOfPeople());
            }
        }
        return map;
    }

    /**
     * 计算每组人均最大还款户数和本金
     * @param list
     * @return
     */
    private  List<DunningMaxRepayNumberAndPrincipal> groupMaxRepayperNumberAndperPrincipalList(List<DunningMaxRepayNumberAndPrincipal> list){
        Map<String, Integer> map = countPeopleOfGroup();
        for (DunningMaxRepayNumberAndPrincipal entity : list){
                //人均
            if (map.get(entity.getNameDealcode()) != 0){
            	DecimalFormat df   = new DecimalFormat("0.00");
            	Double perMaxDealcode = Double.valueOf(entity.getMaxDealcode())/map.get(entity.getNameDealcode());
                entity.setMaxDealcode(df.format(perMaxDealcode)+"");
                Double perPrincipal = Double.valueOf(entity.getPrincipal())/map.get(entity.getNameDealcode());
                entity.setPrincipal(df.format(perPrincipal)+"");
            }
        }
        return list;
    }
}
