package com.ang.reptile.Enum;

public enum MetaTypeEnum {
    JSON(0,"application/json"),FROM_ENCODED(1,"application/x-www-form-urlencoded"),
    FORM_MULTIPART(2,"multipart/form-data");

    private int code;
    private String value;

    MetaTypeEnum(int code, String value) {
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
