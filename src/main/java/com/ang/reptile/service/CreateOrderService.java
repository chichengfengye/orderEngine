package com.ang.reptile.service;

import com.ang.reptile.exception.HttpException;
import com.ang.reptile.model.DataBus;

import java.util.Map;

public interface CreateOrderService {
    DataBus createOrder() throws HttpException;

    boolean updateCookies(Map<String, String> cookies);

    boolean updateCookie(String key, String value);
}
