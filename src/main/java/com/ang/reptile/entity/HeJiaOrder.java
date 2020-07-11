package com.ang.reptile.entity;

import java.util.ArrayList;
import java.util.Date;

public class HeJiaOrder {
    private Long id;
    private String orderInfoName;
    private Integer state;
    private Long createdDate;
    private String orderCode;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    //    private Integer orderNum;//商品数量
//    private Double transportCost;//配送费用
//    private Double orderAmount;//订单总额
//    private Double preferentialAmount;//优惠金额
//    private Double receivableAmount;//应收金额; ---
    private Double payAmount;//实收金额;
    private ArrayList<ItemList> commodityItemList;
    private String jsonStr;
//    private Integer successNum = 0;
    private Integer sumNum = 0;
    private Boolean active = true;
    private Date createdAt;
    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderInfoName() {
        return orderInfoName;
    }

    public void setOrderInfoName(String orderInfoName) {
        this.orderInfoName = orderInfoName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
/*    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }*/

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
/*
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }


    public Double getTransportCost() {
        return transportCost;
    }

    public void setTransportCost(Double transportCost) {
        this.transportCost = transportCost;
    }
*/

/*
    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getPreferentialAmount() {
        return preferentialAmount;
    }

    public void setPreferentialAmount(Double preferentialAmount) {
        this.preferentialAmount = preferentialAmount;
    }

    public Double getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(Double receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
*/

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public ArrayList<ItemList> getCommodityItemList() {
        return commodityItemList;
    }

    public void setCommodityItemList(ArrayList<ItemList> commodityItemList) {
        this.commodityItemList = commodityItemList;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

//    public Integer getSuccessNum() {
//        return successNum;
//    }

//    public void setSuccessNum(Integer successNum) {
//        this.successNum = successNum;
//    }

    public Integer getSumNum() {
        return sumNum;
    }

    public void setSumNum(Integer sumNum) {
        this.sumNum = sumNum;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "HeJiaOrder{" +
                "createdDate=" + createdDate +
//                ", state='" + state + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
//                ", orderNum=" + orderNum +
//                ", transportCost=" + transportCost +
//                ", orderAmount=" + orderAmount +
//                ", preferentialAmount=" + preferentialAmount +
//                ", receivableAmount=" + receivableAmount +
                ", payAmount=" + payAmount +
                ", jsonStr='" + jsonStr + '\'' +
                ", active=" + active +
                '}';
    }
}
