package com.ang.reptile.Enum;

public enum HttpReqType {
    GET(0,"get"),POST(1,"post");

    private int code;
    private String value;

    HttpReqType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
