package com.ang.reptile.service;

import com.ang.reptile.model.DataBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    @Autowired
    private DoorTracking doorTracking;
    @Autowired
    private OrderCreater orderCreater;

    public DataBus<Object> process() {
        //获取数据并入库
        DataBus dataBus = doorTracking.loopDoorTrackingData();
        if (dataBus.getCode() == DataBus.SUCCESS_CODE) {
            //读取数据发送请求创建订单到帮家
            dataBus = orderCreater.createOrder();
            return dataBus;
        }
        return dataBus;
    }
}
