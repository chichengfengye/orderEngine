package com.ang.reptile.code;

import com.ang.reptile.Enum.BaseEnum;

public enum MessageCode2 implements BaseEnum {
    UNKNOW_ERROR(500,"未知错误！"),
    NEED_REDIRECT(302, "需要重定向！");

    private int code;
    private String value;

    MessageCode2(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String msg) {
        this.value = msg;
    }
}
