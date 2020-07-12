package com.ang.reptile.entity;

import com.ang.reptile.step.ParseOrderStep;

import java.util.Date;

public class MerchentResp {
    private Long id;//serial primary key,
    private String merchentId;//varchar(255) unique COMMENT '源商家的ID',
    private ParseOrderStep steps;// text COMMENT '多种解析步骤',
    private Date createdAt;// datetime,
    private Date updatedAt;// datetime

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchentId() {
        return merchentId;
    }

    public void setMerchentId(String merchentId) {
        this.merchentId = merchentId;
    }

    public ParseOrderStep getSteps() {
        return steps;
    }

    public void setSteps(ParseOrderStep steps) {
        this.steps = steps;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
