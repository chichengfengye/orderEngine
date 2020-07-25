package com.ang.reptile.Enum;

public enum BangJiaOrderStateEnum implements BaseEnum{
    WAIT(0, "等待"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
    RTFAIL(3, "重试失败");

    private int code;
    private String msg;

    BangJiaOrderStateEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return msg;
    }

    public void setValue(String msg) {
        this.msg = msg;
    }

    public static BangJiaOrderStateEnum valueOf(int code) {
        for (BangJiaOrderStateEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }


}
