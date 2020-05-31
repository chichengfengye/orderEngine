package com.ang.reptile.exception;

public class DBException extends MyException{
    public DBException(int code, String msg) {
        super(code, msg);
    }
}
