package com.ang.reptile.util;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.config.HttpConfig;

public class ConfigReader {
    private static final String path = "E:\\projects_2\\order\\config.json";

    public static HttpConfig getConfig(String path) {
        FileReader fileReader = new FileReader(path);
        String result = fileReader.readString();
        HttpConfig httpConfig = JSONObject.parseObject(result, HttpConfig.class);
        return httpConfig;
    }

    public static JSONObject getJSONConfig(String path) {
        FileReader fileReader = new FileReader(path);
        String result = fileReader.readString();
        return JSON.parseObject(result);

    }
}
