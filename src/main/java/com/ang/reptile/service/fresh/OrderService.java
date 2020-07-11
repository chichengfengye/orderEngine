package com.ang.reptile.service.fresh;

import com.ang.reptile.model.DataBus;

import java.util.List;

public interface OrderService {
    /**
     * 获取指定上游的数据
     * @return
     */
    DataBus<Object> getOrders(List<String> merchentIDs);


    /**
     * 转换上游数据到下游数据
     * @return
     */
    DataBus<Object> truncateOrders();


    /**
     * 创建下游订单数据
     * @return
     */
    DataBus<Object> createOrders(List<String> merchentIDs);
}
