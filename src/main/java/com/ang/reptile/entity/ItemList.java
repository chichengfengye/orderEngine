package com.ang.reptile.entity;

public class ItemList {
    private Double amount;//总价
    private String commodityInfoName;
    private Integer commodityInfoNum;
    private Double commodityInfoPrice;//商品单价
    private String skucode;//: "3010"
    private String summary;//: "冰箱除味杀菌高温清洗服务"

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCommodityInfoName() {
        return commodityInfoName;
    }

    public void setCommodityInfoName(String commodityInfoName) {
        this.commodityInfoName = commodityInfoName;
    }

    public Integer getCommodityInfoNum() {
        return commodityInfoNum;
    }

    public void setCommodityInfoNum(Integer commodityInfoNum) {
        this.commodityInfoNum = commodityInfoNum;
    }

    public Double getCommodityInfoPrice() {
        return commodityInfoPrice;
    }

    public void setCommodityInfoPrice(Double commodityInfoPrice) {
        this.commodityInfoPrice = commodityInfoPrice;
    }

    public String getSkucode() {
        return skucode;
    }

    public void setSkucode(String skucode) {
        this.skucode = skucode;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
