package com.ang.reptile.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.config.ConfigReader;
import com.ang.reptile.service.OrderTypeService;
import org.springframework.stereotype.Service;

@Service
public class OrderTypeServiceImpl implements OrderTypeService {

    private JSONObject orderTypeConfig;

    public OrderTypeServiceImpl() {
        this.orderTypeConfig = ConfigReader.getJSONConfig("orderType_mapper.json");
    }

    @Override
    public String getServiceCode(String name) {
        return "165064";//this.orderTypeConfig.getJSONObject(name).getString("code");

    }
}
