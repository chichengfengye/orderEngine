package com.ang.reptile.entity;

import com.ang.reptile.Enum.BangJiaOrderStateEnum;
import com.ang.reptile.annotation.NotEncoded;

import java.util.Date;

public class BangJiaOrder {

    private Long id;
    private String hejiaOrderCode;
    private String ordertype;//服务项目
    private Integer number_0;//该商品数量
    private String servicemode = "上门";//服务方式 ,如 上门
    private Integer guarantee = 1;//工单类型 编码 1
    private String guaranteeName = "保内";//工单类型，文字描述
    private String originname;//工单来源 originno
    private String factorynumber;//原厂单号
    private String repairdate;//上门时间: 2020-05-05 15:36:09
    private Double price;//上门收费
    private String username;
    private String mobile;//用户电话
    private String province;// 1
    private String city;// 2 天津市
    private String county;// 12  房山区
    private String town;// 50000172 琉璃河镇（街道）
    private String address;//详细地址: 立教
    private Double mbuyprice;//购买价格 --对应实收金额
    private String mbuydate;//购买日期: 2020-05-05
    private String createname;
    private String note;//备注

    private BangJiaOrderStateEnum state;
    private Date createdAt;
    private Date updatedAt;


    @NotEncoded
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHejiaOrderCode() {
        return hejiaOrderCode;
    }

    public void setHejiaOrderCode(String hejiaOrderCode) {
        this.hejiaOrderCode = hejiaOrderCode;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public Integer getNumber_0() {
        return number_0;
    }

    public void setNumber_0(Integer number_0) {
        this.number_0 = number_0;
    }

    public String getServicemode() {
        return servicemode;
    }

    public void setServicemode(String servicemode) {
        this.servicemode = servicemode;
    }

    public Integer getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(Integer guarantee) {
        this.guarantee = guarantee;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public String getOriginname() {
        return originname;
    }

    public void setOriginname(String originname) {
        this.originname = originname;
    }

    public String getFactorynumber() {
        return factorynumber;
    }

    public void setFactorynumber(String factorynumber) {
        this.factorynumber = factorynumber;
    }

    public String getRepairdate() {
        return repairdate;
    }

    public void setRepairdate(String repairdate) {
        this.repairdate = repairdate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getMbuyprice() {
        return mbuyprice;
    }

    public void setMbuyprice(Double mbuyprice) {
        this.mbuyprice = mbuyprice;
    }

    public String getMbuydate() {
        return mbuydate;
    }

    public void setMbuydate(String mbuydate) {
        this.mbuydate = mbuydate;
    }

    public String getCreatename() {
        return createname;
    }

    public void setCreatename(String createname) {
        this.createname = createname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @NotEncoded
    public BangJiaOrderStateEnum getState() {
        return state;
    }

    public void setState(BangJiaOrderStateEnum state) {
        this.state = state;
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
