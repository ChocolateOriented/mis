package com.mo9.risk.modules.dunning.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.mo9.risk.modules.dunning.enums.PayStatus;
import java.util.Date;

/**
 * Created by jxli on 2017/11/7.
 * 还款流水信息
 */
public class SerialRepay {

	private RepayWay repayWay;//还款方式
	private String repayChannel;//还款渠道
	private PayStatus repayStatus ;//还款状态
	private String repayAmount ;//还款金额
	private String payType;//还款类型
	private Date repayTime;//还款时间
	private Date statusTime;//状态改变时间/回调时间/更新时间

	public RepayWay getRepayWay() {
		return repayWay;
	}

	@JSONField(deserialize = false)
	public void setRepayWay(RepayWay repayWay) {
		this.repayWay = repayWay;
	}

	public String getRepayChannel() {
		return repayChannel;
	}

	public void setRepayChannel(String repayChannel) {
		this.repayChannel = repayChannel;
	}

	public PayStatus getRepayStatus() {
		return repayStatus;
	}

	@JSONField(deserialize = false)
	public void setRepayStatus(PayStatus repayStatus) {
		this.repayStatus = repayStatus;
	}

	public String getRepayAmount() {
		return repayAmount;
	}

	public void setRepayAmount(String repayAmount) {
		this.repayAmount = repayAmount;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType)
	{
		this.payType = payType;
	}
	public String getPaytypeText() {
		return "loan".equals(this.payType) ?  "全款" :
				"delay".equals(this.payType) ?  "续期" + this.payType :
						"partial".equals(this.payType) ?  "部分" : "";
	}

	public Date getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}

	public Date getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(Date statusTime) {
		this.statusTime = statusTime;
	}

	@Override
	public String toString() {
		return "SerialRepay{" +
				"repayWay='" + repayWay + '\'' +
				", repayChannel='" + repayChannel + '\'' +
				", repayStatus='" + repayStatus + '\'' +
				", repayAmount='" + repayAmount + '\'' +
				", payType='" + payType + '\'' +
				", repayTime=" + repayTime +
				", statusTime=" + statusTime +
				'}';
	}

	public enum RepayWay {
		AGENCY_COLLECT("代收"),
		AGENCY_DEDUCT("代扣"),
		SELF_OFFLINE("线下自主还款");

		private String desc;

		RepayWay(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
}
