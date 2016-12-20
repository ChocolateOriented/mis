package com.mo9.risk.modules.dunning.entity;

import java.util.Date;

/**
 *  部分还款订单
 * Created by sun on 2016/8/3.
 */
public class PartialOrder {
    /**
     *  任务ID
     */
    public String taskId;
    /**
     *  部分还款金额
     */
    public int repayAmount;
    /**
     * 部分还款时间
     */
    public Date repayTime;
    /**
     * 还款订单
     */
    public String repayDealcode;
    /**
     * 部分订单父订单状态
     */
    public String parentDealStatus;

}
