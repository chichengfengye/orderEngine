package com.ang.reptile.exception;

public class MyException extends Exception {
    private String msg;
    private int code;

    public MyException() {

    }

    public MyException(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
