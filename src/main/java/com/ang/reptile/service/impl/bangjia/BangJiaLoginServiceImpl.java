package com.ang.reptile.service.impl.bangjia;

import com.ang.reptile.Enum.UpOrDownStream;
import com.ang.reptile.code.MessageCode;
import com.ang.reptile.config.DownStreamLoginChecker;
import com.ang.reptile.config.Login;
import com.ang.reptile.exception.HttpException;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.service.LoginService;
import com.ang.reptile.util.http.MyHttpRequestBuilder;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("loginBangJia")
public class BangJiaLoginServiceImpl implements LoginService {
    @Autowired
    private DownStreamLoginChecker downStreamLoginChecker;

    public DataBus<Map<String, String>> login(UpOrDownStream stream) throws HttpException {
        Map<HttpUrl, List<Cookie>> res = new HashMap<>();
        String urlTarget = null;
        String urlPath = null;
        HashMap<HttpUrl, List<Cookie>> map = null;
        if (stream == UpOrDownStream.DOWN_STREAM) {//下游
            Login login = downStreamLoginChecker.getLogin();
            String url = login.getLoginUrl();
            MyHttpRequestBuilder myHttpRequestBuilder = MyHttpRequestBuilder.createReqInstance();
            myHttpRequestBuilder.addParameters(login.getParams());
            myHttpRequestBuilder.buildPostMultiPartForm(url);
            Response response = myHttpRequestBuilder.execute();
            List<String> keys = downStreamLoginChecker.getKeyWorlds().getKeys();

            if (response.code() == 302 && response.header("Location").equals("/index.php/Ucenter/index.html")) {
                try {
                    map = myHttpRequestBuilder.getCookieStore();
                } catch (Exception e) {
                    e.printStackTrace();
                    return DataBus.failure(MessageCode.Code.LOGIN_FAIL, MessageCode.Message.LOGIN_FAIL);
                }
            } else {
                return DataBus.failure(MessageCode.Code.LOGIN_FAIL, MessageCode.Message.LOGIN_FAIL);
            }
        } else {

        }

        Map<String, String> result = new HashMap<>();
        for (HttpUrl httpUrl : map.keySet()) {
            List<Cookie> cookies = map.get(httpUrl);
            for (Cookie cookie : cookies) {
                result.put(cookie.name(), cookie.value());
            }
        }
        return DataBus.success(result);
    }
}
