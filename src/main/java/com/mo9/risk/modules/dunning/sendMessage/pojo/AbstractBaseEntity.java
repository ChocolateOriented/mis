package com.mo9.risk.modules.dunning.sendMessage.pojo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.mo9.risk.modules.dunning.sendMessage.dao.IEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by qtzhou on 2017/11/9.
 * 实体类基类
 */
public abstract class AbstractBaseEntity implements IEntity, Serializable {

    /**
     * 数据库生成id
     *
     */
    @Id
    @Null(message = "创建时不允许指定 id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 数据记录创建时间
     */
    @Null(message = "创建时不允许指定 createTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 数据记录更新时间
     */
    @Null(message = "创建时不允许指定 updateTime")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;



    /**
     * 是否软删除
     * TODO：默认值为0，怎么防止创建记录时输入其他值？
     */
    @Null(message = "创建时不允许指定 isDeleted")
    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
