package com.mo9.risk.modules.dunning.bean.dto;

import java.util.Date;

/**
 * Created by sun on 2017/6/8.
 */
public class Mo9MqMessage {

  private Date sendTime = new Date();

  private int index = -1;

  private String remark = "";

  public Date getSendTime() {
    return sendTime;
  }

  public void setSendTime(Date sendTime) {
    this.sendTime = sendTime;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
