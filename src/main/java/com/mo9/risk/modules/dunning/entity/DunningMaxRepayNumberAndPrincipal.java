package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * Created by jxguo on 2017/12/12.
 */
public class DunningMaxRepayNumberAndPrincipal extends DataEntity<DunningMaxRepayNumberAndPrincipal>{

    private Date dateTime;
    private String excelDateTime;
    private String cycle;
    private String nameDealcode;
    private String maxDealcode; //还款最大户数
    private String namePrincipal;
    private String principal;   //最大本金
    private String monthdesc;   //月份内区间

    private Date begintime;
    private Date endtime;

    @ExcelField(title="日期", type=1, align=2, sort=1)
    public String getExcelDateTime() {
        return (DateUtils.formatDate(dateTime,"yyyy-MM") + (monthdesc == null? "": monthdesc));
    }

    public void setExcelDateTime(String excelDateTime) {
        this.excelDateTime = excelDateTime;
    }


    public Date getDateTime() {
        return dateTime;
    }
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @ExcelField(title="催收队列", type=1, align=2, sort=2)
    public String getCycle() {
        return cycle;
    }
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    @ExcelField(title="名称", type=1, align=2, sort=3)
    public String getNameDealcode() {
		return nameDealcode;
	}

	public void setNameDealcode(String nameDealcode) {
		this.nameDealcode = nameDealcode;
	}

	@ExcelField(title="还款最大户数", type=1, align=2, sort=4)
    public String getMaxDealcode() {
        return maxDealcode;
    }

    public void setMaxDealcode(String maxDealcode) {
        this.maxDealcode = maxDealcode;
    }

    @ExcelField(title="名称", type=1, align=2, sort=5)
    public String getNamePrincipal() {
		return namePrincipal;
	}

	public void setNamePrincipal(String namePrincipal) {
		this.namePrincipal = namePrincipal;
	}

    @ExcelField(title="还款最大本金", type=1, align=2, sort=6)
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

	public String getMonthdesc() {
        return monthdesc;
    }

    public void setMonthdesc(String monthdesc) {
        this.monthdesc = monthdesc;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
}
