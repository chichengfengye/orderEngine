package com.ang;

import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class test {
    public static void main(String[] args) {
//        Calendar calendar = Calendar.getInstance();
//        System.out.println(calendar.get(Calendar.HOUR_OF_DAY));
//        System.out.println(NotEncoded.class == NotEncoded.class);
//        System.out.println(NotEncoded.class.isAssignableFrom(NotEncoded.class));
//        BangJiaOrder order = new BangJiaOrder();
//        HashMap<String, String> map = QueryDataMapUtil.getQueryDataMap(order, BangJiaOrder.class);
//        System.out.println(map);
        loginBangJia();

      /*  Map<String, Object> map = new HashMap<String, Object>() {{
            put("username", "LJJ0001");
            put("password", "123456");
        }};
        requestByPostParts(map,"http://wangdian.bangjia.me/index.php/Public/login.html", "utf-8");*/
    }

    public static void loginBangJia() {
        HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                cookieStore.put(httpUrl, list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl);
                return cookies != null ? cookies : new ArrayList<Cookie>();

            }
        }).followRedirects(false);
        OkHttpClient okHttpClient = clientBuilder.build();
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", "LJJ0001")
                .addFormDataPart("password", "123456")
                .build();
        Request request = new Request.Builder().url("http://wangdian.bangjia.me/index.php/Public/login.html")
                .post(requestBody)
                .addHeader("PHPSESSID","lf9fefdsor64ldq7cr7i7he9b5")
                .build();
        try {
            String res;
            Response response = okHttpClient.newCall(request).execute();
            int code = response.code();
            if (code == 302) {
                request = new Request.Builder().url("http://wangdian.bangjia.me/index.php/Public/login.html")
                        .post(requestBody)
                        .build();
                response = okHttpClient.newCall(request).execute();
                code = response.code();
                res = response.body().string();
            }
            res = response.body().string();
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String requestByPostParts(Map<String, Object> map, String url, String charset) {
        HttpClient httpClient = HttpClients.createDefault();;//从连接池中获取
        HttpPost post = new HttpPost(url);
        //post.setHeader("Content-Type", "multipart/form-data");//去掉Header
        BufferedReader br = null;
        try {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (map != null) {
                Iterator iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    multipartEntityBuilder.addTextBody(key, value);
                }
            }
            HttpEntity httpEntity = multipartEntityBuilder.build();
            // 设置请求参数
            post.setEntity(httpEntity);
            // 发起交易
            HttpResponse resp = httpClient.execute(post);
            int ret = resp.getStatusLine().getStatusCode();
            // 响应分析
            HttpEntity entity = resp.getEntity();
            br = new BufferedReader(new InputStreamReader(entity.getContent(), charset));
            StringBuffer responseString = new StringBuffer();
            String result = br.readLine();
            while (result != null) {
                responseString.append(result);
                result = br.readLine();
            }
            return responseString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
