package com.ang.reptile.config;

import lombok.Data;

import java.util.HashMap;

public class HttpConfig {
    private HashMap<String, String> headers;
    private HashMap<String, String> cookies;
    private HashMap<String, String> other;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(HashMap<String, String> cookies) {
        this.cookies = cookies;
    }

    public HashMap<String, String> getOther() {
        return other;
    }

    public void setOther(HashMap<String, String> other) {
        this.other = other;
    }
}
