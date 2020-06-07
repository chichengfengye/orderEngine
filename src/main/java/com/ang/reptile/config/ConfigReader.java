package com.ang.reptile.config;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.config.HttpConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigReader {
    private static String configPath;

    @Value("${config-path}")
    public void setConfigPath(String path) {
        configPath = path;
    }

    public static HttpConfig getHttpConfig(String path) {
        path = generatePath(path);
        FileReader fileReader = new FileReader(path);
        String result = fileReader.readString();
        HttpConfig httpConfig = JSONObject.parseObject(result, HttpConfig.class);
        return httpConfig;
    }

    public static JSONObject getJSONConfig(String path) {
        path = generatePath(path);
        FileReader fileReader = new FileReader(path);
        String result = fileReader.readString();
        return JSON.parseObject(result);
    }

    private static String generatePath(String path) {
        boolean configEndWithSpi = configPath.endsWith("/");
        boolean pathStartWithSpi = path.startsWith("/");
        String result = "";
        if (configEndWithSpi && pathStartWithSpi) {
            result += configPath + path.substring(1);
        } else if ((configEndWithSpi && !pathStartWithSpi)
                || (!configEndWithSpi && pathStartWithSpi)) {
            result = configPath + path;
        } else {
            result = configPath + "/" + path;
        }

        return result;
    }
}
