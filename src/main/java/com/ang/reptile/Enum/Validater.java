package com.ang.reptile.Enum;

import java.util.ArrayList;
import java.util.List;

public class Validater {
    public static Validater BANGJIA_HTML_VALIDATER = new Validater("新增成功");

    private List<String> keys;

    private Validater(){}

    private Validater(List<String> keys) {
        this.keys = new ArrayList<>();
        if (keys != null) {
            this.keys.addAll(keys);
        }
    }

    private Validater(String key) {
        this.keys = new ArrayList<>();
        if (key != null) {
            this.keys.add(key);
        }
    }

    public static Validater buildChecker(String key) {
        return new Validater(key);
    }

    public static Validater buildChecker(List<String> keys) {
        return new Validater(keys);
    }

    public boolean validate(String resultStr) {

        int allNum = keys.size();
        int passNum = 0;
        for (String key : keys) {
            if (resultStr != null && resultStr.contains(key)) {
                passNum++;
            }
        }

        return passNum == allNum ? true : false;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
