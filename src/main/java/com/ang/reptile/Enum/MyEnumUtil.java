package com.ang.reptile.Enum;

public class MyEnumUtil {
    public static <T extends BaseEnum> T getByCode(Integer code, Class<T> t){
        for(T item: t.getEnumConstants()){
            if(item.getCode() == code){
                return item;
            }
        }
        return null;
    }
}
