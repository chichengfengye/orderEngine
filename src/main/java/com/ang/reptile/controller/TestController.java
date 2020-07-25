package com.ang.reptile.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.service.fresh.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/getLvDiOrder")
    public Object getLvDiOrders(@RequestBody List<String> merchentIds) {
        if (merchentIds == null || merchentIds.size() == 0) {
            return DataBus.failure(400,"参数不对！");
        }

        DataBus<Object> dataBus = orderService.getOrders(merchentIds);
        return dataBus;
    }
}
