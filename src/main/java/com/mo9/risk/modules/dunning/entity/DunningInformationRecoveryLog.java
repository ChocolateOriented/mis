package com.mo9.risk.modules.dunning.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by jxguo on 2017/10/31.
 */
public class DunningInformationRecoveryLog extends DataEntity<DunningInformationRecoveryLog>{


    /**
     * 借款人ID
     */
    private int buyerId;
    /**
     * 订单号
     */
    private String dealCode;
    /**
     * 联系方式类型
     */
    private String contactType;
    /**
     * 联系方式
     */
    private String contactNumber;
    /**
     * 联系人和借款人关系
     */
    private String contactRelationship;
    /**
     * 联系人名称
     */
    private String contactName;
    /**
     * 历史记录条数
     */
    private int historyRecordCount;
    /**
     * 操作类型
     */
    private String operationType;

    @NotNull(message = "借贷人Id不能为空")
    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }
    @NotNull(message = "订单号不能为空")
    public String getDealCode() {
        return dealCode;
    }

    public void setDealCode(String dealCode) {
        this.dealCode = dealCode;
    }
    @NotNull(message = "联系方式类型不能为空")
    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
    @NotNull(message = "联系方式不能为空")
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactRelationship() {
        return contactRelationship;
    }

    public void setContactRelationship(String contactRelationship) {
        this.contactRelationship = contactRelationship;
    }

    public int getHistoryRecordCount() {
        return historyRecordCount;
    }

    public void setHistoryRecordCount(int historyRecordCount) {
        this.historyRecordCount = historyRecordCount;
    }

    public String getOperationType() {
        return operationType;
    }
    @NotNull(message = "操作类型不能为空")
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
