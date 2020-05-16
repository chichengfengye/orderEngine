package com.ang.reptile.service;

import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.code.MessageCode;
import com.ang.reptile.config.HttpConfig;
import com.ang.reptile.dto.DBQueryPage;
import com.ang.reptile.mapper.HeJiaOrderMapper;
import com.ang.reptile.model.Address;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.pojo.CreaterOrder;
import com.ang.reptile.pojo.HeJiaOrder;
import com.ang.reptile.pojo.ItemList;
import com.ang.reptile.util.CityUtil;
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
    private CityUtil cityUtil;
    private HttpConfig httpConfig;
    private JSONObject orderTypeConfig;
    private HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    private HashMap<String, String> cookies = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();
    SimpleDateFormat repairDateFormat = new SimpleDateFormat("yy:MM:dd HH:mm:ss");
    SimpleDateFormat buyDateFormat = new SimpleDateFormat("yy-MM-dd");

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
            conclude = DataBus.SUCCESS();
        } else {
            conclude = new DataBus(2, "部分成功【" + successNum + "】，部分失败【" + errorNum
                    + "】！", null);
        }


        return conclude;
    }

    private void loadConfigs() {
        httpConfig = ConfigReader.getConfig("E:\\projects_2\\order\\downstream_http.json");
        this.headers = httpConfig.getHeaders();
        this.cookies = httpConfig.getCookies();

        cityUtil.load("E:\\projects_2\\order\\address_mapper.json");
        this.orderTypeConfig = ConfigReader.getJSONConfig("E:\\projects_2\\order\\orderType_mapper.json");
    }

    //发送请求到帮家
    public DataBus postToBangJia(HeJiaOrder heJiaOrder) {
        int flag = 0;//0 失败 1 成功 2 请求成功但是更新数据库失败！
        String message = null;
        //todo 创建http客户端
        DataBus<List<CreaterOrder>> dataBus = truncateOrder(heJiaOrder);
        if (dataBus.getCode() != DataBus.SUCCESS_CODE) {
            return dataBus;
        }
        List<CreaterOrder> createrOrders = dataBus.getData();
        int successItem = 0;
        int itemSum = createrOrders.size();
        for (CreaterOrder createrOrder : createrOrders) {
            //        MediaType mediaType = MediaType.parse("multipart/form-data");
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

            } catch (Exception e) {
                logger.error("============= 创建订单失败！============");
                e.printStackTrace();
                return DataBus.failure();
            }

            //解析结果 判断是否有字段 “新增成功”
            if (resultStr != null && resultStr.contains("新增成功")) {
                //更新数据库的active字段
                try {
                    successItem++;
                } catch (Exception e) {
                    e.printStackTrace();
                    flag = 2;
                    message = "创建订单成功！但是更新数据库失败！orderId【" + heJiaOrder.getId() + "】";
                    logger.error(message);
                }
            } else {
                flag = 0;
            }
        }

        if (itemSum == successItem) {
            flag = 1;
            message = "成功！";
        } else if (successItem > 0 && itemSum != successItem) {
            message = "部分成功部分失败！";
            flag = 2;
        } else {
            message = "全部失败！";
            flag = 0;
        }

        heJiaOrder.setActive(false);
        heJiaOrder.setSuccessNum(successItem);

        int res = mapper.updateStateById(heJiaOrder);

        //响应结果
        return  new DataBus(flag, message, null);
    }

    private DataBus<List<CreaterOrder>> truncateOrder(HeJiaOrder heJiaOrder) {
        List<CreaterOrder> orders = new ArrayList<>();
        //原数据
        Date buyDate = new Date(heJiaOrder.getCreatedDate());
        String address = heJiaOrder.getReceiverAddress();
        String userName = heJiaOrder.getReceiverName();
        String mobile = heJiaOrder.getReceiverPhone();
        String orderInfoName = heJiaOrder.getOrderInfoName();
        Double payAmount = heJiaOrder.getPayAmount();
        List<ItemList> itemLists = heJiaOrder.getCommodityItemList();


        //目标数据
        Date repaireDate = getRepairedDate(buyDate);//repaireDateCalender.getTime();
        //todo bug
        String orderType ="1";// orderTypeConfig.getJSONObject(orderInfoName).getString("code");//服务项目
        Address addressConfig = CityUtil.getAddress(address);
        if (addressConfig == null) {
            logger.error("===>找不到address【{}】的配置省市地址！", address);
            return DataBus.failure(MessageCode.Code.ADDRESS_NOT_MATCHED, MessageCode.Message.ADDRESS_NOT_MATCHED);
        }
        String province = addressConfig.getProvince().getCode();
        String city = addressConfig.getCity().getCode();
        String county = addressConfig.getCounty().getCode();
        String town = addressConfig.getTown().getCode();

        for (ItemList itemList : itemLists) {
            Double amount = itemList.getAmount();
            String commodityInfoName = itemList.getCommodityInfoName();
            Integer commodityInfoNum = itemList.getCommodityInfoNum();
            Double commodityInfoPrice = itemList.getCommodityInfoPrice();
            String skucode = itemList.getSkucode();
            String summary = itemList.getSummary();

            CreaterOrder createrOrder = new CreaterOrder();
            createrOrder.setNumber_0(commodityInfoNum);
            createrOrder.setOrdertype(orderType);
            createrOrder.setServicemode("上门");
            createrOrder.setGuarantee(1);
            createrOrder.setOriginname("自接单");
            createrOrder.setFactorynumber(orderInfoName);
            createrOrder.setPrice(0d);
            createrOrder.setRepairdate(repairDateFormat.format(repaireDate));
            createrOrder.setUsername(userName);
            createrOrder.setMobile(mobile);
            createrOrder.setProvince(province);
            createrOrder.setCity(city);
            createrOrder.setCounty(county);
            createrOrder.setTown(town);
            createrOrder.setAddress(address);
            createrOrder.setMbuyprice(amount);
            createrOrder.setCreatename("李昂");
            createrOrder.setMbuydate(buyDateFormat.format(buyDate));

            orders.add(createrOrder);
        }



        return DataBus.success(orders);
    }

    /**
     * 获取上门时间
     * 1. 等于下单时间的时间+2天
     * 2. 下单时间过了18点，就要+3
     * 3. 上门时间的小时可以随便写，我这里就默认是0了
     * @param buyDate
     * @return
     */
    private Date getRepairedDate(Date buyDate) {
        Calendar repaireDateCalender = Calendar.getInstance();
        repaireDateCalender.setTime(buyDate);
        //检查是否>=18点
        int hour = repaireDateCalender.get(Calendar.HOUR_OF_DAY);
        boolean isOffWork  = false;
        if (hour >= 18) {
            isOffWork = true;
        }
        int offset = isOffWork ? 3 : 2;
        repaireDateCalender.add(Calendar.DAY_OF_MONTH, offset);
        return repaireDateCalender.getTime();
    }


}
