package com.ang.reptile.service;

import com.ang.reptile.Enum.UpOrDownStream;
import com.ang.reptile.exception.HttpException;
import com.ang.reptile.model.DataBus;

import java.util.Map;

public interface LoginService {
    DataBus<Map<String, String>> login(UpOrDownStream stream) throws HttpException;
}
