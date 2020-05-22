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
import com.ang.reptile.config.ConfigReader;
import com.ang.reptile.util.QueryDataMapUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CreateOrderService {
    private Logger logger = LoggerFactory.getLogger(CreateOrderService.class);

    @Autowired
    private HeJiaOrderMapper mapper;
    @Autowired
    private OrderTypeService orderTypeService;

    private CityUtil cityUtil;
    private HttpConfig httpConfig;

    private HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
    private HashMap<String, String> cookies = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();
    SimpleDateFormat repairDateFormat = new SimpleDateFormat("yy:MM:dd HH:mm:ss");
    SimpleDateFormat buyDateFormat = new SimpleDateFormat("yy-MM-dd");

    public CreateOrderService() {
        loadConfigs();
    }

    public DataBus createOrder() {

        DataBus conclude = null;
        long errorNum = 0L;
        long successNum = 0L;
        long totalOrder = 0l;
        long id = 0L;
        DBQueryPage page = new DBQueryPage();
        page.setPageSize(30);


        //轮循获取数据
        while (true) {
            page.setId(id);
            List<HeJiaOrder> hejiaOrders = mapper.getHeJiaOrders(page, true);
            if (hejiaOrders == null || hejiaOrders.size() == 0) {
                if (totalOrder == 0l) {
                    logger.info("没有合家订单用于创建！");
                }
                break;
            } else {
                id = hejiaOrders.get(hejiaOrders.size() - 1).getId();
                for (HeJiaOrder hejiaOrder : hejiaOrders) {
                    totalOrder++;
                    DataBus result = postToBangJia(hejiaOrder);
                    if (result.getCode() != DataBus.SUCCESS_CODE) {
                        logger.error(result.getMsg());
                        errorNum++;
                    } else {
                        successNum++;
                    }
                }
            }
        }


        if (totalOrder == 0l) {
            conclude = DataBus.SUCCESS("没有合家订单需要创建！");
        } else if (successNum == totalOrder) {
            conclude = DataBus.SUCCESS();
        } else if (successNum == 0l) {
            conclude = DataBus.failure();
        } else {
            conclude = new DataBus(2, "部分成功【" + successNum + "】，部分失败【" + errorNum
                    + "】！", null);
        }


        return conclude;
    }

    private void loadConfigs() {
        this.httpConfig = ConfigReader.getConfig("downstream_http.json");
        this.headers = httpConfig.getHeaders();
        this.cookies = httpConfig.getCookies();

        cityUtil.load("address_mapper.json");

    }

    public boolean updateCookie(String key, String value) {
        if (this.cookies == null) {
            this.cookies = new HashMap<>();
        }

        this.cookies.put(key, value);
        return true;
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

        logger.info("==>本次创建帮家订单目标数量：{}", createrOrders.size());

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
                logger.error("============= 创建订单失败！订单所属合家订单的编码 {}============", createrOrder.getHeJiaOrderCode());
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
                    message = "创建订单成功！但是更新数据库失败！合家orderId【" + heJiaOrder.getId() + "】合家orderCode " + "createrOrder.getHeJiaOrderCode()";
                    logger.error(message);
                }
            } else {
                message = resultStr;
                logger.info(message);
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

        logger.info("==>实际创建帮家订单数量：{}", successItem);

        //响应结果
        return new DataBus(flag, message, null);
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
        String code = heJiaOrder.getOrderCode();


        //目标数据
        Date repaireDate = getRepairedDate(buyDate);//repaireDateCalender.getTime();
        //todo bug
        String orderType = orderTypeService.getServiceCode(orderInfoName);//;// //服务项目
        Address addressConfig = CityUtil.getAddress(address);
        if (addressConfig == null) {
            logger.error("===>找不到address【{}】的配置省市地址！", address);
            return DataBus.failure(MessageCode.Code.ADDRESS_NOT_MATCHED, MessageCode.Message.ADDRESS_NOT_MATCHED);
        }
        String province = addressConfig.getProvince().getCode();
        String city = addressConfig.getCity().getCode();
        String county = addressConfig.getCounty().getCode();
        String town = addressConfig.getTown().getCode();
        String note = "订单名称：" + orderInfoName + "\n 所属合家订单编码:" + code;

        for (ItemList itemList : itemLists) {
            Double amount = itemList.getAmount();
            String commodityInfoName = itemList.getCommodityInfoName();
            Integer commodityInfoNum = itemList.getCommodityInfoNum();
            Double commodityInfoPrice = itemList.getCommodityInfoPrice();
            String skucode = itemList.getSkucode();
            String summary = itemList.getSummary();

            CreaterOrder createrOrder = new CreaterOrder();
            createrOrder.setHeJiaOrderCode(code);
            createrOrder.setNumber_0(commodityInfoNum);
            createrOrder.setOrdertype(orderType);
            createrOrder.setServicemode("上门");
            createrOrder.setGuarantee(1);
            createrOrder.setOriginname(addressConfig.getVillage());//工单来源 指的是哪个小区来的工单--2020年5月17日
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
            createrOrder.setCreatename("靳丰");
            createrOrder.setMbuydate(buyDateFormat.format(buyDate));
            createrOrder.setNote(note);

            orders.add(createrOrder);
        }


        return DataBus.success(orders);
    }

    /**
     * 获取上门时间
     * 1. 等于下单时间的时间+2天
     * 2. 下单时间过了18点，就要+3
     * 3. 上门时间的小时可以随便写，我这里就默认是0了
     *
     * @param buyDate
     * @return
     */
    private Date getRepairedDate(Date buyDate) {
        Calendar repaireDateCalender = Calendar.getInstance();
        repaireDateCalender.setTime(buyDate);
        //检查是否>=18点
        int hour = repaireDateCalender.get(Calendar.HOUR_OF_DAY);
        boolean isOffWork = false;
        if (hour >= 18) {
            isOffWork = true;
        }
        int offset = isOffWork ? 3 : 2;
        repaireDateCalender.add(Calendar.DAY_OF_MONTH, offset);
        return repaireDateCalender.getTime();
    }


}
