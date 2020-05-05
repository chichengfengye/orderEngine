package com.ang.reptile.controller;

import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.model.Message;
import com.ang.reptile.service.DoorTracking;
import com.ang.reptile.service.OrderCreater;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private DoorTracking doorTracking;
    @Autowired
    private OrderCreater orderCreater;


    @RequestMapping("/getData")
    public Message getData() {
        try {
            DataBus<List<String>> dataBus = doorTracking.loopDoorTrackingData();
            if (dataBus.getCode() == DataBus.SUCCESS_CODE) {
                return Message.success(dataBus.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return Message.failure();
    }

    @RequestMapping("/test")
    public Object test(@RequestBody HashMap jsonObject) {
        System.out.println(jsonObject);
        return jsonObject;
    }

    @RequestMapping("/createOrder")
    public Object createOrder() {
        return orderCreater.createOrder();
    }
}
