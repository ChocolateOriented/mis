package com.mo9.risk.modules.dunning.sendMessage;

/**
 * Created by qtzhou on 2017/11/9.
 * 当前发送的消息状态
 */
public enum MessageStatusEnum {

    SUCCESS("成功"),
    FAILED("失败");

    private String status;

    public String getStatus() {
        return this.status;
    }

    MessageStatusEnum(String status) {
        this.status = status;
    }

    @Override
    public String toString () {
        return "{\"key\":" + "\"" + this.name() + "\"," + "\"value\":" + "\"" + this.status + "\"}";
    }
}
