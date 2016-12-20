package com.mo9.risk.modules.dunning.entity;

/**
 * Created by sun on 2016/7/4.
 */
public enum DunningTaskStatus {

    DUNNING(0), //催款中
    EXPIRED(1), //到期
    FINISHED(2),//完成
    CHANGE(3); //同期转移

    private int code;

    DunningTaskStatus(int code) {
        this.code = code;
    }

}
