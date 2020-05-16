package com.ang.reptile.service;

import com.alibaba.fastjson.JSON;
import com.ang.reptile.config.HttpConfig;
import com.ang.reptile.mapper.HeJiaOrderMapper;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.model.Page;
import com.ang.reptile.pojo.HeJiaOrder;
import com.ang.reptile.pojo.ItemList;
import com.ang.reptile.util.*;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoorTracking {
    @Autowired
    private HeJiaOrderMapper mapper;
    private Logger logger = LoggerFactory.getLogger(DoorTracking.class);
    private String reqUrl = "";
    private static final int pageSize = 15;
    private HashMap<String, String> headers = new HashMap<>();
    private static HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    private static HashMap<String, String> cookieMap = new HashMap<>();

    /**
     * 1. 创建excel并且追加标题头
     * 2. 获取数据，循环插入数据到数据库和excel
     * 3. 关闭excel
     *
     * @return
     */
    public DataBus<List<String>> loopDoorTrackingData() {
        HttpConfig httpConfig = ConfigReader.getConfig("E:\\projects_2\\order\\upstream_http.json");

        HashMap<String, String> cookies = httpConfig.getCookies();
        if (cookies != null && cookies.size() > 0) {
            cookieMap = cookies;
        }

        HashMap<String, String> headers = httpConfig.getHeaders();
        if (headers != null && headers.size() > 0) {
            this.headers = headers;
        }

        this.reqUrl = httpConfig.getUrl();

        int allDataSize = 0;
        int allDBItemSize = 0;

        int currentPage = 0;
        int totalPage = 1;
        Page page = new Page(currentPage, pageSize);

        while (currentPage <= totalPage) {
            currentPage += 1;
            page.setCurrentPage(currentPage);
            //http获取数据
            DataBus<String> dataBus = getDoorTrackingData(page);
            if (dataBus.getCode() == DataBus.SUCCESS_CODE) {
                DataParseUtil.Result result = DataParseUtil.parseToPojo(dataBus.getData());
                if (result == null) {
                    logger.info("===> 解析出的返回数据不合格！");
                    break;
                }

                //分页用参数
                totalPage = result.getTotalPage();
                List<String> datas = result.getDatas();

                logger.debug("========= 获取{}条记录==========", datas.size());
                try {
                    allDataSize += datas.size();
                    logger.debug("=============开始插入数据库，一共{}条==============", datas.size());
                    allDBItemSize += insertSmOrderByString(datas);
                } catch (Exception e) {
                    logger.error("============== 插入数据库失败！=================");
                    e.printStackTrace();
                    return DataBus.failure();
                }
            }
        }


        logger.info("+++++++++++共获取苏宁 " + allDataSize + "条数据+++++++++++++");
        logger.info("+++++++++++共插入数据库 " + allDBItemSize + "条记录+++++++++++++");

        DataBus<List<String>> dataBus = DataBus.SUCCESS();

        return dataBus;
    }

    private DataBus<String> getDoorTrackingData(Page page) {
        int pageSize = page.getPageSize();
        int currentPage = page.getCurrentPage();
        Page queryData = new Page();
        queryData.setCurrentPage(currentPage);
        queryData.setPageSize(pageSize);
//        FormBody.Builder builder = new FormBody.Builder();
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
            DataBus<String> dataBus = DataBus.SUCCESS();
            dataBus.setData(resultStr);
            return dataBus;

        } catch (Exception e) {
            logger.error("============= 查询数据失败！============");
            e.printStackTrace();
            return DataBus.failure();
        }

    }

    private int insertSmOrderByString(List<String> datas) throws Exception {
        int success = 0;
        for (String data : datas) {
            HeJiaOrder order = JSON.parseObject(data, HeJiaOrder.class);
            try {
                order.setJsonStr(data);
                List<ItemList> list = order.getCommodityItemList();
                order.setSumNum(list == null ? 0 : list.size());
                mapper.insert(order);
                success++;
            } catch (DuplicateKeyException se) {
//                se.printStackTrace();
                logger.error("==============插入数据重复！！！=================", order.getOrderCode());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("==============插入数据库出错！！！=================", order.toString());
            }
        }
        return success;
    }
}
