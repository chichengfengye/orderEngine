package com.ang.reptile.service;

import com.ang.reptile.model.DataBus;

import java.util.List;

public interface GetOrderService {
    DataBus<List<String>> loopDoorTrackingData();

    int insertSmOrderByString(List<String> datas) throws Exception;
}
