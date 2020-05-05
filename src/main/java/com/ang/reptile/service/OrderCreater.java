package com.ang.reptile.service;

import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.config.HttpConfig;
import com.ang.reptile.dto.DBQueryPage;
import com.ang.reptile.mapper.HeJiaOrderMapper;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.pojo.CreaterOrder;
import com.ang.reptile.pojo.HeJiaOrder;
import com.ang.reptile.util.ConfigReader;
import com.ang.reptile.util.QueryDataMapUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderCreater {
    private Logger logger = LoggerFactory.getLogger(OrderCreater.class);

    @Autowired
    private HeJiaOrderMapper mapper;
    private HttpConfig httpConfig;
    private JSONObject addressConfig;
    private JSONObject orderTypeConfig;
    private HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    private HashMap<String, String> cookies = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();

    public DataBus createOrder() {
        loadConfigs();

        DataBus conclude = null;
        Long errorNum = 0L;
        Long successNum = 0L;
        Long id = 0L;
        DBQueryPage page = new DBQueryPage();
        page.setPageSize(30);
        //轮循获取数据
        while (true) {
            page.setId(id);
            List<HeJiaOrder> hejiaOrders = mapper.getHeJiaOrders(page, true);
            if (hejiaOrders == null || hejiaOrders.size() == 0) {
                successNum = 1l;
                break;
            } else {
                id = hejiaOrders.get(hejiaOrders.size() - 1).getId();
                for (HeJiaOrder hejiaOrder : hejiaOrders) {
                    DataBus result = postToBangJia(hejiaOrder);
                    if (result.getCode() != DataBus.SUCCESS_CODE) {
                        errorNum++;
                    } else {
                        successNum++;
                    }
                }
            }
        }

        if (errorNum > 0 && successNum == 0) {
            conclude = DataBus.failure();
        } else if (errorNum == 0 && successNum > 0) {
            conclude = DataBus.success();
        } else {
            conclude = new DataBus(2, "部分成功【" + successNum + "】，部分失败【" + errorNum
                    + "】！", null);
        }


        return conclude;
    }

    private void loadConfigs() {
        httpConfig = ConfigReader.getConfig("E:\\projects_2\\order\\create_order.json");
        this.headers = httpConfig.getHeaders();
        this.cookies = httpConfig.getCookies();

        this.addressConfig = ConfigReader.getJSONConfig("E:\\projects_2\\order\\address_mapper.json");
        this.orderTypeConfig = ConfigReader.getJSONConfig("E:\\projects_2\\order\\create_order.json");
    }

    //发送请求到帮家
    public DataBus postToBangJia(HeJiaOrder heJiaOrder) {
        int flag = 0;//0 失败 1 成功 2 请求成功但是更新数据库失败！
        String message = null;
        //todo 创建http客户端
        CreaterOrder createrOrder = truncateOrder(heJiaOrder);
        MediaType mediaType = MediaType.parse("multipart/form-data");
        FormBody.Builder builder = new FormBody.Builder();
        HashMap<String, String> urlEncodedMap = QueryDataMapUtil.getQueryDataMap(createrOrder, CreaterOrder.class);
        if (urlEncodedMap != null && urlEncodedMap.size() >= 0) {
            for (Map.Entry<String, String> stringObjectEntry : urlEncodedMap.entrySet()) {
                String name = stringObjectEntry.getKey();
                String value = stringObjectEntry.getValue();
                builder.add(name, value);
            }
        }
        FormBody body = builder.build();
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

        //cookie
        String cookieValue = cookies.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(";"));
        //header
        Headers.Builder headersBuilder = new Headers.Builder();
        headersBuilder.add("Cookie", cookieValue);
        for (Map.Entry<String, String> header : this.headers.entrySet()) {
            headersBuilder.add(header.getKey(), header.getValue());
        }
        Headers headers = headersBuilder.build();

        //do req
        Request request = new Request.Builder()
                .url(httpConfig.getUrl())
                .post(body)
                .headers(headers)
                .build();

        //发送请求
        String resultStr = null;
        try (Response response = okHttpClient.newCall(request).execute()) {
            resultStr = response.body().string();
            DataBus<String> dataBus = DataBus.success();
            dataBus.setData(resultStr);

        } catch (Exception e) {
            logger.error("============= 创建订单失败！============");
            e.printStackTrace();
            return DataBus.failure();
        }

        //解析结果 判断是否有字段 “新增成功”
        if (resultStr != null && resultStr.contains("新增成功")) {
            //更新数据库的active字段
            try {
                heJiaOrder.setActive(false);
                int res = mapper.updateStateById(heJiaOrder);
                flag = 1;
                message = "成功！";
            } catch (Exception e) {
                e.printStackTrace();
                flag = 2;
                message = "创建订单成功！但是更新数据库失败！orderId【" + heJiaOrder.getId() + "】";
                logger.error(message);
            }
        } else {
            flag = 0;
        }

        //响应结果
        return  new DataBus(flag, message, null);
    }

    private CreaterOrder truncateOrder(HeJiaOrder heJiaOrder) {
        Long orderTime = heJiaOrder.getCreatedDate();
        String address = heJiaOrder.getReceiverAddress();
        Double amount = heJiaOrder.getPayAmount();
        String userName = heJiaOrder.getReceiverName();
        String mobile = heJiaOrder.getReceiverPhone();
        String orderInfoName = heJiaOrder.getOrderInfoName();

        //todo 这里的处理有bug
        String orderType = orderTypeConfig.getJSONObject(orderInfoName).getString("code");
        String repairdate = "";

        String province = "1";
        String city = "2";
        String county = "7";
        String town = "50000042";
        String createdName = "李昂";

        CreaterOrder createrOrder = new CreaterOrder();
        createrOrder.setOrdertype(orderType);
        createrOrder.setServicemode("上门");
        createrOrder.setGuarantee(1);
        createrOrder.setOriginname("自接单");
        createrOrder.setFactorynumber(orderInfoName);
        createrOrder.setPrice(1000.222d);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy:MM:dd HH:mm:ss");
        createrOrder.setRepairdate(simpleDateFormat.format(date));
        createrOrder.setUsername("罗杰");
        createrOrder.setMobile("12345678911");
        createrOrder.setProvince(province);
        createrOrder.setCity(city);
        createrOrder.setCounty(county);
        createrOrder.setTown(town);
        createrOrder.setAddress("你好啊的地址");
        createrOrder.setMbuyprice(1334.23);
        createrOrder.setCreatename(createdName);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yy-MM-dd");
        createrOrder.setMbuydate(simpleDateFormat2.format(date));

        return createrOrder;
    }

}
