//package com.ang.reptile.service.impl.lvdi;
//
//import com.alibaba.fastjson.JSON;
//import com.ang.reptile.config.ConfigReader;
//import com.ang.reptile.config.HttpConfig;
//import com.ang.reptile.mapper.HeJiaOrderMapper;
//import com.ang.reptile.mapper.LvdiOrderMapper;
//import com.ang.reptile.model.DataBus;
//import com.ang.reptile.model.Page;
//import com.ang.reptile.pojo.HeJiaOrder;
//import com.ang.reptile.pojo.ItemList;
//import com.ang.reptile.pojo.LvDiOrder;
//import com.ang.reptile.service.GetOrderService;
//import com.ang.reptile.service.impl.GetOrderServiceImpl;
//import com.ang.reptile.service.impl.hejia.GetOrderServiceHeJia;
//import com.ang.reptile.util.DataParseUtil;
//import com.ang.reptile.util.QueryDataMapUtil;
//import okhttp3.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service("lvDiOrderService")
//public class GetOrderServiceLvDi extends GetOrderServiceImpl {
//    @Autowired
//    private LvdiOrderMapper mapper;
//    private Logger logger = LoggerFactory.getLogger(GetOrderServiceLvDi.class);
////    private String reqUrl = "";
////    private static final int pageSize = 15;
////    private HashMap<String, String> headers = new HashMap<>();
//    private static HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
////    private static HashMap<String, String> cookieMap = new HashMap<>();
//
//    @Override
//    public int storeOrderToDB(List<String> datas) throws Exception {
//        int success = 0;
//        for (String data : datas) {
//            LvDiOrder order = JSON.parseObject(data, LvDiOrder.class);
//            try {
//                List<ItemList> list = order.getCommodityItemList();
//                order.setSumNum(list == null ? 0 : list.size());
//                mapper.insert(order);
//                success++;
//            } catch (DuplicateKeyException se) {
////                se.printStackTrace();
//                logger.error("==============插入数据重复！！！=================", order.getOrderCode());
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error("==============插入数据库出错！！！=================", order.toString());
//            }
//        }
//        return success;
//    }
//
//    @Override
//    protected DataBus<String> getDoorTrackingData(Page page) {
//        int pageSize = page.getPageSize();
//        int currentPage = page.getCurrentPage();
//        Page queryData = new Page();
//        queryData.setCurrentPage(currentPage);
//        queryData.setPageSize(pageSize);
//
//        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//        HashMap<String, String> json = QueryDataMapUtil.getQueryDataMap(queryData, Page.class);
//        String jsonStr = JSON.toJSONString(json);//"{\"name\":\"金凤\"}";
//        RequestBody body = RequestBody.create(mediaType, jsonStr);
//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().cookieJar(new CookieJar() {
//            @Override
//            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
//                cookieStore.put(httpUrl, list);
//            }
//
//            @Override
//            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
//                List<Cookie> cookies = cookieStore.get(httpUrl);
//                return cookies != null ? cookies : new ArrayList<Cookie>();
//
//            }
//        });
//        OkHttpClient okHttpClient = clientBuilder.build();
//
//        String cookieValue = cookieMap.entrySet().stream()
//                .map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining(";"));
//        //header
//        Headers.Builder headersBuilder = new Headers.Builder();
//        headersBuilder.add("Cookie", cookieValue);
//        for (Map.Entry<String,String> header : this.headers.entrySet()) {
//            headersBuilder.add(header.getKey(), header.getValue());
//        }
//        Headers headers = headersBuilder.build();
//
//        //do req
//        Request request = new Request.Builder()
//                .url(reqUrl)
//                .post(body)
//                .headers(headers)
//                .build();
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            String resultStr = response.body().string();
//            DataBus<String> dataBus = DataBus.success();
//            dataBus.setData(resultStr);
//            return dataBus;
//
//        } catch (Exception e) {
//            logger.error("============= 查询数据失败！============");
//            e.printStackTrace();
//            return DataBus.failure();
//        }
//
//    }
//}
