package com.ang.reptile.service;

import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.config.ConfigReader;
import org.springframework.stereotype.Service;

@Service
public class OrderTypeService {
    private JSONObject orderTypeConfig;

    public OrderTypeService() {
        this.orderTypeConfig = ConfigReader.getJSONConfig( "orderType_mapper.json");
    }

    public String getServiceCode(String name) {
        return "165064";//this.orderTypeConfig.getJSONObject(name).getString("code");

    }
}
