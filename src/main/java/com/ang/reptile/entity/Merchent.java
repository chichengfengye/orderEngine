package com.ang.reptile.entity;

import java.util.Date;

public class Merchent {
    private Long id ;//serial primary  key,
    private String uuid ;//varchar(255) unique COMMENT '唯一ID',
    private String nick_name ;//varchar(255) COMMENT '商家名称',
    private String table_name ;//varchar(255) COMMENT '该商家对应的实体数据表的核心名称关键字',
    private String status ;//int COMMENT '商家状态，0 禁用 1启用',
    private Date created_at ;//datetime,
    private Date updated_at ;//datetime

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
