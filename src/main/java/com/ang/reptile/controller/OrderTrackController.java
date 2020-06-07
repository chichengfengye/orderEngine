package com.ang.reptile.controller;

import com.ang.reptile.model.DataBus;
import com.ang.reptile.model.Message;
import com.ang.reptile.service.GetOrderService;
import com.ang.reptile.service.impl.HeJiaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order/track")
public class OrderTrackController {
    @Autowired
    @Qualifier("heJiaOrderService")
    private GetOrderService heJiaOrderService;

    @RequestMapping("/heJia")
    public Message getData() {
        try {
            DataBus<List<String>> dataBus = heJiaOrderService.loopDoorTrackingData();
            if (dataBus.getCode() == DataBus.SUCCESS_CODE) {
                return Message.success(dataBus.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return Message.failure();
    }



}
