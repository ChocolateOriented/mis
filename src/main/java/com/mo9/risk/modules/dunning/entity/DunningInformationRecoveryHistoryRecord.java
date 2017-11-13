package com.mo9.risk.modules.dunning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Created by jxguo on 2017/10/31.
 */
public class DunningInformationRecoveryHistoryRecord extends DataEntity<DunningInformationRecoveryHistoryRecord>{
    /**
     * 订单id
     */
    private String dealCode;
    /**
     * 借贷人ID
     */
    private int buyerId;
    /**
     * 备注
     */
    private String historyRemark;


    private String createtime;

    @NotNull(message = "订单号不能为空")
    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }

    @NotNull(message = "借贷人id不能为空")
    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }


    @Length(min=1, max=50, message="备注长度必须介于 1 和 50 之间")
    public String getHistoryRemark() {
        return historyRemark;
    }

    public void setHistoryRemark(String historyRemark) {
        this.historyRemark = historyRemark;
    }
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",  timezone = "GMT+8")
    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
