package com.ang.reptile.Enum;

public enum HttpProtocolEnum implements BaseEnum{
    HTTP(0,"http"), HTTPS(1, "https");

    private int code;
    private String value;

    HttpProtocolEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public int getCode() {
        return code;
    }
    @Override
    public void setCode(int code) {
        this.code = code;
    }
    @Override
    public String getValue() {
        return value;
    }
    @Override
    public void setValue(String value) {
        this.value = value;
    }

  /*  public static HttpProtocolEnum valueOf(int code) {
        for (HttpProtocolEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }*/
}
