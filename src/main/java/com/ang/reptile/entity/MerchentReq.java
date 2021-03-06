package com.ang.reptile.entity;

import com.ang.reptile.Enum.HttpProtocolEnum;
import com.ang.reptile.Enum.MerchentStatusEnum;
import com.ang.reptile.Enum.MetaTypeEnum;

import java.util.Date;
import java.util.Map;

public class MerchentReq {
    Long id;// serial primary key,
    String uuid;// varchar(255) unique COMMENT '商家的唯一ID',
    HttpProtocolEnum protocol;// int COMMENT '协议，0 http 1 https',
    String domain;// 域名
    String url;// varchar(255) COMMENT '请求网址（不包括协议在内的地址）',
    MerchentStatusEnum type ;//int COMMENT '请求类型，0 get，1 post',
    MetaTypeEnum metaType ;//int COMMENT '参数类型，0 application/json, 1 application/x-www-form-urlencoded, 2 multipart/form-data ',
    Map<String,String> args;// text COMMENT '参数key，json',
    Map<String,String> cookies;//COMMENT 'cookies',
    Map<String,String> headers;// text COMMENT 'headers',
    Date createdAt;// datetime,
    Date updatedAt;// datetime

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

    public HttpProtocolEnum getProtocol() {
        return protocol;
    }

    public void setProtocol(HttpProtocolEnum protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MerchentStatusEnum getType() {
        return type;
    }

    public void setType(MerchentStatusEnum type) {
        this.type = type;
    }

    public MetaTypeEnum getMetaType() {
        return metaType;
    }

    public void setMetaType(MetaTypeEnum metaType) {
        this.metaType = metaType;
    }

    public Map<String,String> getArgs() {
        return args;
    }

    public void setArgs(Map<String,String> args) {
        this.args = args;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
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
