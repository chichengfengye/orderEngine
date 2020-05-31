package com.ang.reptile.controller;

import com.ang.reptile.Enum.UpOrDownStream;
import com.ang.reptile.exception.HttpException;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.model.Message;
import com.ang.reptile.service.GetOrderService;
import com.ang.reptile.service.CreateOrderService;
import com.ang.reptile.service.LoginService;
import com.ang.reptile.service.TruncateOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private GetOrderService getOrderService;
    @Autowired
    private CreateOrderService createOrderService;
    @Autowired
    private TruncateOrderService truncateOrderService;
    @Autowired
    private LoginService loginService;


    @RequestMapping("/getData")
    public Message getData() {
        try {
            DataBus<List<String>> dataBus = getOrderService.loopDoorTrackingData();
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
        try {
            truncateOrderService.truncateOrder();

            return createOrderService.createOrder();
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return DataBus.failure();
    }

    @RequestMapping("/updateCookie")
    public Object setCookie(String key, String cookie) {
        return createOrderService.updateCookie(key, cookie);
    }

    @RequestMapping("/loginBangjia")
    public Object setCookie() throws HttpException {
        return loginService.login(UpOrDownStream.DOWN_STREAM);
    }

}
