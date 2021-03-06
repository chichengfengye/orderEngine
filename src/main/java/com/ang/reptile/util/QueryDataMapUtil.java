package com.ang.reptile.util;

import com.ang.reptile.annotation.NotEncoded;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

public class QueryDataMapUtil {
    public static HashMap<String, String> getQueryDataMapLoopSuperClazz(Object object, Class<?> clazz) {
        HashMap<String, String> map = new HashMap<>();
        Class<?> clazzParent = null;
        do {
            clazzParent = clazzParent == null ? clazz : clazzParent;
            map = getMap(map, object, clazzParent);

        } while ((clazzParent = clazz.getSuperclass()) != null);

        return map;
    }
    public static HashMap<String, String> getQueryDataMap(Object object, Class<?> clazz) {
        HashMap<String, String> map = new HashMap<>();
        return getMap(map, object, clazz);
    }
    private static HashMap<String, String> getMap(HashMap<String, String> map, Object object, Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        //获取get方法
        try {
            for (Method method : methods) {
                String name = method.getName();
                if (name.startsWith("get")) {
                    name = name.replaceFirst("get","");
                    name = name.substring(0, 1).toLowerCase() + name.substring(1);
                    boolean couldEncoded = couldEncoded(method);
                    if (couldEncoded) {
                        Object value = method.invoke(object);
                        String valueStr = "";
                        if (value instanceof Number || value instanceof String || value instanceof Boolean) {
                            valueStr = "" + value;
                        } else {
                            if (value == null) {
                                valueStr = "";
                            } else {
                                valueStr = value.toString();
                            }
                        }
                        map.put(name, valueStr);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private static boolean couldEncoded(Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations != null) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation annotation = annotations[i];
//                if (annotation.annotationType().getName().equals(NotEncoded.class.getName())) {
//                    return false;
//                }
                if (annotation.annotationType() == NotEncoded.class) {
                    return false;
                }
            }
        }
        return true;
    }
}
