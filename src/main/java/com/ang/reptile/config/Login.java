package com.ang.reptile.config;

import java.util.Map;

public class Login {
    private Map<String, String> params;
    private String loginUrl;// http://wangdian.bangjia.me/index.php/Public/login.html

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
