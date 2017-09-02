package com.mo9.risk.modules.dunning.bean;


import com.mo9.risk.modules.dunning.entity.TRiskBuyerContactRecords;
import java.util.List;

/**
 * Created by jxli on 2017/9/2.
 * 江湖救急响应结果
 */
public class RiskContactRecordResponse extends RiskResponse{

	public ContactRecordResponseDatas datas;

	public ContactRecordResponseDatas getDatas() {
		return datas;
	}

	public void setDatas(ContactRecordResponseDatas datas) {
		this.datas = datas;
	}

	@Override
	public String toString() {
		return "RiskContactRecordResponse{" +
				"datas=" + datas +
				", resultCode='" + resultCode + '\'' +
				", result='" + result + '\'' +
				", resultMsg='" + resultMsg + '\'' +
				'}';
	}

	public class ContactRecordResponseDatas {

		List<TRiskBuyerContactRecords> callLog ;
		String mobile;

		public List<TRiskBuyerContactRecords> getCallLog() {
			return callLog;
		}

		public void setCallLog(List<TRiskBuyerContactRecords> callLog) {
			this.callLog = callLog;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		@Override
		public String toString() {
			return "Datas{" +
					"callLog=" + callLog +
					", mobile='" + mobile + '\'' +
					'}';
		}
	}
}
