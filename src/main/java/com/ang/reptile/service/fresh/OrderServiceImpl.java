package com.ang.reptile.service.fresh;

import com.alibaba.fastjson.JSON;
import com.ang.reptile.code.MessageCode;
import com.ang.reptile.entity.MerchentReq;
import com.ang.reptile.mapper.MerchentMapper;
import com.ang.reptile.mapper.MerchentReqMapper;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.model.Page;
import com.ang.reptile.util.QueryDataMapUtil;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private MerchentMapper merchentMapper;

    @Autowired
    private MerchentReqMapper merchentReqMapper;

    /**
     * 获取指定上游的数据
     * @return
     */
    @Override
    public DataBus<Object> getOrders(List<String> merchentIDs){
        //1. 获取指定的上游的请求参数
        try {
            List<MerchentReq> merchentReqs = merchentReqMapper.getByIds(merchentIDs);
            if (merchentReqs == null || merchentReqs.size() == 0) {
                return DataBus.success(MessageCode.Code.MERCHENT_REQ_NOT_FOUND,MessageCode.Message.MERCHENT_REQ_NOT_FOUND);
            }

            //2. 使用请求参数去请求数据回来并入库
            for (MerchentReq merchentReq : merchentReqs) {
                getOrderByOKHttp(merchentReq);
            }

            return DataBus.success();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DataBus.failure();
    }

    /**
     * 获取订单并保存到db
     * @param merchentReq
     * @return
     */
    private Object getOrderByOKHttp(MerchentReq merchentReq) throws Exception{
        //轮询请求机制，直到请求的数据为空集合的时候停止
//        For1mBody.Builder builder = new FormBody.Builder();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        HashMap<String, String> json = QueryDataMapUtil.getQueryDataMap(queryData, Page.class);
        String jsonStr = JSON.toJSONString(json);//"{\"name\":\"金凤\"}";
        RequestBody body = RequestBody.create(mediaType, jsonStr);
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
        });
        OkHttpClient okHttpClient = clientBuilder.build();
//        RequestBody body = RequestBody.create(URL_ENCODED, builder);

        //cookie
        String cookieValue = cookieMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(";"));
        //header
        Headers.Builder headersBuilder = new Headers.Builder();
        headersBuilder.add("Cookie", cookieValue);
        for (Map.Entry<String,String> header : this.headers.entrySet()) {
            headersBuilder.add(header.getKey(), header.getValue());
        }
        Headers headers = headersBuilder.build();

        //do req
        Request request = new Request.Builder()
                .url(reqUrl)
                .post(body)
                .headers(headers)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            String resultStr = response.body().string();
            DataBus<String> dataBus = DataBus.success();
            dataBus.setData(resultStr);
            return dataBus;

        } catch (Exception e) {
            logger.error("============= 查询数据失败！============");
            e.printStackTrace();
            return DataBus.failure();
        }

        return null;
    }


    /**
     * 转换上游数据到下游数据
     * @return
     */
    @Override
    public DataBus<Object> truncateOrders(){

        return DataBus.failure();
    }


    /**
     * 创建下游订单数据
     * @return
     */
    @Override
    public DataBus<Object> createOrders(List<String> merchentIDs){

        return DataBus.failure();
    }






}
