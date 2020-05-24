package com.ang.reptile.util.http;

public enum Validater {
    BANGJIA_HTML_VALIDATER;

    public boolean validate(String resultStr) {
        return (resultStr != null && resultStr.contains("新增成功"));
    }
}
