package com.ang.reptile.util;

import cn.hutool.core.io.file.FileReader;
import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.config.Config;
import org.springframework.util.StringUtils;

public class ConfigReader {
    private static final String path = "E:\\projects_2\\order\\config.json";

    public static Config getConfig() {
        FileReader fileReader = new FileReader(path);
        String result = fileReader.readString();
        Config config = JSONObject.parseObject(result, Config.class);
        return config;
    }
}
