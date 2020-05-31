package com.ang.reptile.exception;

public class HttpException extends MyException {
    public HttpException(int code, String msg) {
        super(code, msg);
    }

}
