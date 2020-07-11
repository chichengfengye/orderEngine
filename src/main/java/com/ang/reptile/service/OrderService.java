package com.ang.reptile.service;

import com.ang.reptile.model.DataBus;

import java.util.List;

public interface OrderService {

    /**
     * 获取全部订单
     * @return
     */
    DataBus<List<String>> loopData();

    /**
     * 数据入库
     * @param datas
     * @return
     * @throws Exception
     */
    int storeOrderToDB(List<String> datas) throws Exception;
}
