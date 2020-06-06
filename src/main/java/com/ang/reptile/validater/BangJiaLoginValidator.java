package com.ang.reptile.validater;

import com.ang.reptile.Enum.Validater;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class BangJiaLoginValidator extends Validater {


    public BangJiaLoginValidator(List<String> keys) {
        super(keys);
    }

    @Override
    public boolean validate(Response response) {
        if (response.code() == 302) {
            try {
                String resultStr = response.body().string();
                int sum = keys.size();
                int success = 0;
                for (String key : keys) {
                    if (resultStr.contains(key)) {
                        success++;
                    }
                }

                return success == sum ? true : false;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return false;
    }
}
