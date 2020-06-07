package com.ang.reptile.controller;

import com.ang.reptile.exception.HttpException;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.service.CreateOrderService;
import com.ang.reptile.service.TruncateOrderService;
import com.ang.reptile.service.impl.TruncateOrderServiceBangJia;
import com.ang.reptile.service.impl.CreateOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/create")
public class OrderCreateController {
    @Autowired
    private TruncateOrderService truncateOrderServiceBangJia;

    @Autowired
    private CreateOrderService createOrderServiceImpl;

    @RequestMapping("/bangJia")
    public Object createOrder() {
        try {
            truncateOrderServiceBangJia.truncateOrder();

            return createOrderServiceImpl.createOrder();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return DataBus.failure();
    }
}
