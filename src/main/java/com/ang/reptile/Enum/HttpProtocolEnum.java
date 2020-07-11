package com.ang.reptile.Enum;

public enum HttpProtocolEnum {
    HTTP(0,"http"), HTTPS(1, "https");

    private int code;
    private String value;

    HttpProtocolEnum(int code, String value) {
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
