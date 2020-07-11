package com.ang.reptile.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LvDiOrder {
    private Long id ;//serial primary  key,
    private String OrderId ;//varchar(255) COMMENT '订单ID',
    private String OrderNo ;//varchar(255) COMMENT '订单编号',
    private String AddTime ;//varchar(255) COMMENT '下单时间',
    private String ReceiverName ;//varchar(255) COMMENT '收件人',
    private Integer ReceiverMobile ;//int COMMENT '收件号码',
    private String ReceiverAddress;// varchar(255) COMMENT '收件地址',
    private Integer Status ;//int COMMENT '订单状态',
    private Date createdAt; //datetime,
    private Date updatedAt;// datetime
}
