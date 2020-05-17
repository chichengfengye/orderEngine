package com.ang.reptile.util;

import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.model.Address;
import com.ang.reptile.model.NameCode;
import com.ang.reptile.config.ConfigReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CityUtil {
    private static JSONObject addressConfig;
    private static List<String> blocks;

    public static void load(String path) {
        addressConfig = ConfigReader.getJSONConfig(path);
        blocks = new ArrayList<>();
        for (Map.Entry<String, Object> stringObjectEntry : addressConfig.entrySet()) {
            blocks.add(stringObjectEntry.getKey());
        }
    }

    public static String getBlockName(String address) {
        for (String block : blocks) {
            if (address.contains(block)) {
                return block;
            }
        }

        return null;
    }

    public static Address getAddress(String address) {
        String blockName = getBlockName(address);
        if (blockName != null) {
            for (String key : addressConfig.keySet()) {
                if (key.contains(address)||address.contains(key)) {
                    address = key;
                }
            }
            if (address == null) {
                return null;
            }
            JSONObject jsonObject = addressConfig.getJSONObject(address);
            NameCode province = jsonObject.getObject("province", NameCode.class);
            NameCode city = jsonObject.getObject("city", NameCode.class);
            NameCode county = jsonObject.getObject("county", NameCode.class);
            NameCode town = jsonObject.getObject("town", NameCode.class);
            return new Address(address, province, city, county, town);
        }
        return null;
    }

}
