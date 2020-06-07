package com.ang.reptile.controller;

import com.ang.reptile.Enum.UpOrDownStream;
import com.ang.reptile.exception.HttpException;
import com.ang.reptile.service.LoginService;
import com.ang.reptile.service.impl.BangJiaLoginServiceImpl;
import com.ang.reptile.service.impl.CreateOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {
    @Autowired
    private CreateOrderServiceImpl createOrderServiceImpl;

    @Autowired
    @Qualifier("loginBangJia")
    private LoginService bangJiaLoginServiceImpl;

    @RequestMapping("/updateCookie")
    public Object setCookie(String key, String cookie) {
        return createOrderServiceImpl.updateCookie(key, cookie);
    }

    @RequestMapping("/loginBangjia")
    public Object setCookie() throws HttpException {
        return bangJiaLoginServiceImpl.login(UpOrDownStream.DOWN_STREAM);
    }
}
