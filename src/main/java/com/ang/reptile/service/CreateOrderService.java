package com.ang.reptile.service;

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
import com.ang.reptile.util.http.MyHttpRequestBuilder;
import com.ang.reptile.util.http.Validater;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CreateOrderService {
    private Logger logger = LoggerFactory.getLogger(CreateOrderService.class);

    @Autowired
    private HeJiaOrderMapper mapper;
    @Autowired
    private OrderTypeService orderTypeService;
    private boolean retry = false;

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

    /**
     * 第一次创建上游订单到下游
     *
     * @return
     */
    public DataBus<HashMap<String, Integer>> createFirst() {
        DataBus conclude;
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
                    } else {
                        successNum++;
                    }
                }
            }
        }


        if (totalOrder == 0l) {
            conclude = DataBus.failure(MessageCode.Code.UPSTREAM_ACTIVEORDER_NOTFOUND, MessageCode.Message.UPSTREAM_ACTIVEORDER_NOTFOUND);
        } else if (successNum == totalOrder) {
            conclude = DataBus.success();
        } else if (successNum == 0l) {
            conclude = DataBus.failure();
        } else {
            conclude = new DataBus(MessageCode.Code.CREATE_ORDER_PART_SUCCESS,
                    MessageCode.Message.CREATE_ORDER_PART_SUCCESS,
                    null);
        }


        return conclude;
    }

    public DataBus createOrder() {
        DataBus<HashMap<String, Integer>> dataBus = createFirst();
        int code = dataBus.getCode();
        if (code == DataBus.SUCCESS_CODE) {
            return dataBus;
        } else {
            if (retry) {
                return retryCreateOrder();
            } else {
                return dataBus;
            }
        }
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
        DataBus<List<CreaterOrder>> dataBus = truncateOrder(heJiaOrder);
        if (dataBus.getCode() != DataBus.SUCCESS_CODE) {
            return dataBus;
        }
        List<CreaterOrder> createrOrders = dataBus.getData();
        int successItem = 0;
        int itemSum = createrOrders.size();
        logger.info("==>本次需要创建帮家订单数量：{}", itemSum);

        for (CreaterOrder createrOrder : createrOrders) {
            MyHttpRequestBuilder myHttpRequestBuilder = MyHttpRequestBuilder.createFormReqInstance(Validater.BANGJIA_HTML_VALIDATER);
            HashMap<String, String> urlEncodedMap = QueryDataMapUtil.getQueryDataMap(createrOrder, CreaterOrder.class);
            myHttpRequestBuilder.addParameters(urlEncodedMap);
            myHttpRequestBuilder.addCookies(this.cookies);
            myHttpRequestBuilder.addHeaders(this.headers);
            myHttpRequestBuilder.build(httpConfig.getUrl());
            DataBus<String> reqResult = myHttpRequestBuilder.execute();
            if (reqResult.getCode() == DataBus.SUCCESS_CODE) {
                successItem++;
            } else {
                logger.error("============= 创建订单失败！订单所属合家订单的编码 {}============", createrOrder.getHeJiaOrderCode());
                //todo 将错误的订单入库

            }
        }

        heJiaOrder.setActive(false);
        heJiaOrder.setSuccessNum(successItem);

        int res = mapper.updateStateById(heJiaOrder);

        logger.info("==>成功创建帮家订单数量：{}", successItem);

        //响应结果
        return new DataBus(flag, message, null);
    }

    /**
     * 重试创建订单
     *
     * @return
     */
    public DataBus retryCreateOrder() {
        //todo 查询失败的下级订单

        //todo 用查询的结果再次创建订单

        //todo 仅仅重试一次，失败则单独记录重试失败的日志

        return DataBus.failure();
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
