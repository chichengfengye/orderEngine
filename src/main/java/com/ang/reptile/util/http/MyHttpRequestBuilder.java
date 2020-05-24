package com.ang.reptile.util.http;

import com.ang.reptile.model.DataBus;
import okhttp3.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyHttpRequestBuilder {

    private OkHttpClient okHttpClient;
    private HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    private HashMap<String, String> urlEncodedMap = new HashMap<>();
    private HashMap<String, String> cookies = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();
    private Request request;
    private Validater validater;


    private MyHttpRequestBuilder(){}

    public static MyHttpRequestBuilder createFormReqInstance(Validater validater) {
        MyHttpRequestBuilder myHttpRequestBuilder = new MyHttpRequestBuilder();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                myHttpRequestBuilder.cookieStore.put(httpUrl, list);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                List<Cookie> cookies = myHttpRequestBuilder.cookieStore.get(httpUrl);
                return cookies != null ? cookies : new ArrayList<Cookie>();

            }
        });
        OkHttpClient okHttpClient = clientBuilder.build();
        myHttpRequestBuilder.okHttpClient = okHttpClient;
        myHttpRequestBuilder.validater = validater;
        return myHttpRequestBuilder;

    }

    public MyHttpRequestBuilder build(String url) {
        //parameters formBody
        FormBody formBody = buildFormBody();
        //cookies headers
        Headers headers = buildHeadersAndCookies();
        //headers
        this.request = new Request.Builder()
                .url(url)
                .post(formBody)
                .headers(headers)
                .build();

        return this;
    }

    private Headers buildHeadersAndCookies() {
        Headers.Builder headersBuilder = new Headers.Builder();
        String cookieValue = cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(";"));
        headersBuilder.add("Cookie", cookieValue);
        for (Map.Entry<String, String> header : this.headers.entrySet()) {
            headersBuilder.add(header.getKey(), header.getValue());
        }
        Headers headers = headersBuilder.build();
        return headers;
    }

    private FormBody buildFormBody() {
        FormBody.Builder builder = new FormBody.Builder();

        if (urlEncodedMap != null && urlEncodedMap.size() >= 0) {
            for (Map.Entry<String, String> stringObjectEntry : urlEncodedMap.entrySet()) {
                String name = stringObjectEntry.getKey();
                String value = stringObjectEntry.getValue();
                builder.add(name, value);
            }
        }
        FormBody body = builder.build();
        return body;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public void addParameters(HashMap<String, String> urlEncodedMap) {
        if (this.urlEncodedMap == null) {
            this.urlEncodedMap = new HashMap<>();
        }
        this.urlEncodedMap.putAll(urlEncodedMap);

    }

    public void addCookies(HashMap<String, String> cookies) {
        this.cookies.putAll(cookies);
    }

    public void addHeaders(HashMap<String, String> headers) {
        this.headers.putAll(headers);
    }

    public DataBus<String> execute() {
        String resultStr;
        try (Response response = this.okHttpClient.newCall(this.request).execute()) {
            resultStr = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return DataBus.failure(e.getMessage());
        }

        //检验是否是成功的
        boolean isSuccess = validater.validate(resultStr);

        return isSuccess ? DataBus.success() : DataBus.failure(resultStr);

    }
}