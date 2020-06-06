package com.ang.reptile.Enum;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

public abstract class Validater {
    protected List<String> keys;

    protected Validater() {

    }

    protected Validater(List<String> keys) {
        this.keys = new ArrayList<>();
        if (keys != null) {
            this.keys.addAll(keys);
        }
    }

    protected Validater(String key) {
        this.keys = new ArrayList<>();
        if (key != null) {
            this.keys.add(key);
        }
    }

    public abstract boolean validate(okhttp3.Response response);

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
