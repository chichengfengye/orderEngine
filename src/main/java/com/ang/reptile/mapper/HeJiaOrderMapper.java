package com.ang.reptile.mapper;

import com.ang.reptile.model.Page;
import com.ang.reptile.pojo.HeJiaOrder;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeJiaOrderMapper {
    int insert(HeJiaOrder heJiaOrder);

    List<HeJiaOrder> getHeJiaOrders(@Param("page") Page page, @Param("active") Boolean active);
}
