package com.ang.reptile.Enum;

public enum MerchentStatusEnum implements BaseEnum{
    ACTIVE(1,"激活"),FORBIDDEN(2, "禁用");

    private int code;
    private String value;

    MerchentStatusEnum(int code, String value) {
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
