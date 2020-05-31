package com.ang.reptile.service;

import com.ang.reptile.Enum.UpOrDownStream;
import com.ang.reptile.Enum.Validater;
import com.ang.reptile.code.MessageCode;
import com.ang.reptile.config.DownStreamLoginChecker;
import com.ang.reptile.config.KeyWorlds;
import com.ang.reptile.config.Login;
import com.ang.reptile.exception.HttpException;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.util.http.MyHttpRequestBuilder;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoginService {
    @Autowired
    private DownStreamLoginChecker downStreamLoginChecker;

    public boolean LoginCheck(UpOrDownStream stream) throws HttpException {
        if (stream == UpOrDownStream.DOWN_STREAM) {//下游
            KeyWorlds keyWorlds = downStreamLoginChecker.getKeyWorlds();
            String url = keyWorlds.getUrl();
            MyHttpRequestBuilder myHttpRequestBuilder = MyHttpRequestBuilder.createReqInstance(Validater.buildChecker(keyWorlds.getKeys()));
            myHttpRequestBuilder.buildGet(url);
            DataBus<String> dataBus = myHttpRequestBuilder.execute();
            if (dataBus.getCode() == DataBus.SUCCESS_CODE) {
                return true;
            } else {
                return false;
            }
        } else {

        }


        return false;
    }

    public DataBus<Map<String, String>> login(UpOrDownStream stream) throws HttpException {
        Map<HttpUrl, List<Cookie>> res = new HashMap<>();
        String urlTarget = null;
        String urlPath = null;
        List<Cookie> cookies = null;
        if (stream == UpOrDownStream.DOWN_STREAM) {//下游
            Login login = downStreamLoginChecker.getLogin();
            String url = login.getLoginUrl();
            MyHttpRequestBuilder myHttpRequestBuilder = MyHttpRequestBuilder.createReqInstance(Validater.buildChecker(downStreamLoginChecker.getKeyWorlds().getKeys()));
            myHttpRequestBuilder.addParameters(login.getParams());
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
            myHttpRequestBuilder.addHeaders(headers);
            myHttpRequestBuilder.buildPostForm(url);
            DataBus<String> dataBus = myHttpRequestBuilder.execute();
            if (dataBus.getCode() == DataBus.SUCCESS_CODE) {
                KeyWorlds keyWorlds = downStreamLoginChecker.getKeyWorlds();
                urlTarget = keyWorlds.getUrl();
                for (HttpUrl httpUrl : res.keySet()) {
                    urlPath = httpUrl.url().getPath();
                    if (urlPath.equals(urlTarget)) {
                        cookies = res.get(httpUrl);
                    }
                }
            } else {
                return DataBus.failure(MessageCode.Code.LOGIN_FAIL, MessageCode.Message.LOGIN_FAIL);
            }
        } else {

        }


        Map<String, String> map = cookies.stream().collect(Collectors.toMap(Cookie::name, Cookie::value));
        return DataBus.success(map);
    }
}
