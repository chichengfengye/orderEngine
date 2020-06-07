package com.ang.reptile.service.impl;

import com.ang.reptile.Enum.BangJiaOrderStateEnum;
import com.ang.reptile.Enum.UpOrDownStream;
import com.ang.reptile.code.MessageCode;
import com.ang.reptile.config.HttpConfig;
import com.ang.reptile.dto.DBQueryPage;
import com.ang.reptile.exception.HttpException;
import com.ang.reptile.mapper.BangJiaOrderMapper;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.pojo.BangJiaOrder;
import com.ang.reptile.service.CreateOrderService;
import com.ang.reptile.service.LoginService;
import com.ang.reptile.util.CityUtil;
import com.ang.reptile.config.ConfigReader;
import com.ang.reptile.util.QueryDataMapUtil;
import com.ang.reptile.util.http.MyHttpRequestBuilder;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CreateOrderServiceImpl implements CreateOrderService {
    private Logger logger = LoggerFactory.getLogger(CreateOrderServiceImpl.class);
//    private Logger rtLogger = LoggerFactory.getLogger(CreateOrderService.class);

    //    @Autowired
//    private HeJiaOrderMapper mapper;
    @Autowired
    private OrderTypeServiceImpl orderTypeServiceImpl;
    @Autowired
    @Qualifier("loginBangJia")
    private LoginService bangJiaLoginServiceImpl;
    @Autowired
    private BangJiaOrderMapper bangJiaOrderMapper;
    @Value("${retry}")
    private boolean retry = false;

    private CityUtil cityUtil;
    private HttpConfig httpConfig;

    private HashMap<String, String> cookies = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();

    public CreateOrderServiceImpl() {
        loadConfigs();
    }

    @Override
    public DataBus createOrder() throws HttpException {
        DataBus<HashMap<String, Integer>> dataBus = createFirst();
        int code = dataBus.getCode();
        if (code == DataBus.SUCCESS_CODE) {
            return dataBus;
        }
        if (code == MessageCode.Code.BANGJIA_ORDER_NOT_FOUND) {
            return dataBus;
        } else {
            if (retry) {
                return retryCreateOrder();
            } else {
                return dataBus;
            }
        }
    }

    @Override
    public boolean updateCookies(Map<String, String> cookies) {
        if (this.cookies == null) {
            this.cookies = new HashMap<>();
        }

        this.cookies.putAll(cookies);
        return true;
    }

    @Override
    public boolean updateCookie(String key, String value) {
        if (this.cookies == null) {
            this.cookies = new HashMap<>();
        }

        this.cookies.put(key, value);
        return true;
    }

    //发送请求到帮家
    private DataBus postToBangJia(BangJiaOrder bangJiaOrder, Boolean isRetry) throws HttpException {
        BangJiaOrderStateEnum stateEnum = null;
        int flag = DataBus.FAILURE_CODE;//0 失败 1 成功 2 请求成功但是更新数据库失败！
        String message = null;
        MyHttpRequestBuilder myHttpRequestBuilder = MyHttpRequestBuilder.createReqInstance();
        HashMap<String, String> urlEncodedMap = QueryDataMapUtil.getQueryDataMap(bangJiaOrder, BangJiaOrder.class);
        myHttpRequestBuilder.addParameters(urlEncodedMap);
        myHttpRequestBuilder.addCookies(this.cookies);
        myHttpRequestBuilder.addHeaders(this.headers);
        myHttpRequestBuilder.buildPostForm(httpConfig.getUrl());
        Response response = myHttpRequestBuilder.execute();
        String resultStr = null;
        try {
            resultStr = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resultStr!=null && resultStr.contains("新增成功")) {
            message = "成功";
            stateEnum = BangJiaOrderStateEnum.SUCCESS;
            flag = DataBus.SUCCESS_CODE;
        } else {
            logger.error("============= 创建订单失败！订单所属合家订单的编码 {}============", bangJiaOrder.getHejiaOrderCode());
            logger.debug(resultStr);
            stateEnum = isRetry ? BangJiaOrderStateEnum.RTFAIL : BangJiaOrderStateEnum.FAIL;
            message = "失败！\n" + resultStr;
            flag = DataBus.FAILURE_CODE;
        }
        bangJiaOrder.setState(stateEnum);

        int res = bangJiaOrderMapper.updateStateById(bangJiaOrder);
        //响应结果
        return new DataBus(flag, message, null);
    }

    /**
     * 重试创建订单
     *
     * @return
     */
    private DataBus retryCreateOrder() {
        try {
            //万一错误是由于没有登录呢
            DataBus<Map<String, String>> res = bangJiaLoginServiceImpl.login(UpOrDownStream.DOWN_STREAM);
            if (res.getCode() == DataBus.SUCCESS_CODE) {
                this.updateCookies(res.getData());
            } else {
                return res;
            }
            //查询失败的订单
            long successNum = 0L;
            long totalOrder = 0l;
            long id = 0L;
            DBQueryPage page = new DBQueryPage();
            page.setPageSize(30);

            while (true) {//轮循获取数据
                page.setId(id);
                List<BangJiaOrder> bangjiaOrders = bangJiaOrderMapper.processGetOrders(page, BangJiaOrderStateEnum.FAIL);
                if (bangjiaOrders == null || bangjiaOrders.size() == 0) {
                    if (totalOrder == 0l) {
                        logger.info("没有帮家失败一次的订单用于创建！");
                    }
                    break;
                } else {
                    id = bangjiaOrders.get(bangjiaOrders.size() - 1).getId();
                    for (BangJiaOrder bangJiaOrder : bangjiaOrders) {
                        totalOrder++;
                        DataBus result = postToBangJia(bangJiaOrder, true);
                        if (result.getCode() != DataBus.SUCCESS_CODE) {
                            //todo retry的日志输出到另一个文件
                            logger.error(result.getMsg());
                        } else {
                            successNum++;
                        }
                    }
                }
            }

            if (successNum == totalOrder) {
                return DataBus.success("");
            }

            return DataBus.failure();
        } catch (HttpException e) {
            e.printStackTrace();
            return DataBus.failure(e.getCode(), e.getMsg());
        }

    }

    /**
     * 第一次创建上游订单到下游
     *
     * @return
     */
    private DataBus<HashMap<String, Integer>> createFirst() throws HttpException {
        DataBus conclude;
        long successNum = 0L;
        long totalOrder = 0l;
        long id = 0L;
        DBQueryPage page = new DBQueryPage();
        page.setPageSize(30);

        //轮循获取数据
        while (true) {
            page.setId(id);
            List<BangJiaOrder> bangJiaOrders = bangJiaOrderMapper.processGetOrders(page, BangJiaOrderStateEnum.WAIT);
            if (bangJiaOrders == null || bangJiaOrders.size() == 0) {
                if (totalOrder == 0l) {
                    logger.info("没有帮家订单用于创建！");
                }
                break;
            } else {
                id = bangJiaOrders.get(bangJiaOrders.size() - 1).getId();
                for (BangJiaOrder bangJiaOrder : bangJiaOrders) {
                    totalOrder++;
                    DataBus result = postToBangJia(bangJiaOrder, false);
                    if (result.getCode() != DataBus.SUCCESS_CODE) {
                        logger.error(result.getMsg());
                    } else {
                        successNum++;
                    }
                }
            }
        }


        if (totalOrder == 0l) {
            conclude = DataBus.failure(MessageCode.Code.BANGJIA_ORDER_NOT_FOUND, MessageCode.Message.BANGJIA_ORDER_NOT_FOUND);
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

    private void loadConfigs() {
        this.httpConfig = ConfigReader.getHttpConfig("downstream_http.json");
        this.headers = httpConfig.getHeaders();
        this.cookies = httpConfig.getCookies();

        cityUtil.load("address_mapper.json");

    }
}
