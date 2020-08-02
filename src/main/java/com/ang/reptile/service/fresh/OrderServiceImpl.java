package com.ang.reptile.service.fresh;

import com.ang.reptile.code.MessageCode;
import com.ang.reptile.code.MessageCode2;
import com.ang.reptile.entity.BangJiaOrder;
import com.ang.reptile.entity.MerchentReq;
import com.ang.reptile.mapper.MerchentMapper;
import com.ang.reptile.mapper.MerchentReqMapper;
import com.ang.reptile.mapper.MerchentRespMapper;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.util.QueryDataMapUtil;
import com.ang.reptile.util.http.MyHttpRequestBuilder;
import com.ang.reptile.util.http.UrlUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private MerchentMapper merchentMapper;

    @Autowired
    private MerchentReqMapper merchentReqMapper;

    @Autowired
    private MerchentRespMapper merchentRespMapper;

    /**
     * 获取指定上游的数据
     *
     * @return
     */
    @Override
    public DataBus<Object> getOrders(List<String> merchentIDs) {
        //1. 获取指定的上游的请求参数
        try {
            Map<String, Object> res = new HashMap<>();

            List<MerchentReq> merchentReqs = merchentReqMapper.getByIds(merchentIDs);
            if (merchentReqs == null || merchentReqs.size() == 0) {
                return DataBus.success(MessageCode.Code.MERCHENT_REQ_NOT_FOUND, MessageCode.Message.MERCHENT_REQ_NOT_FOUND);
            }

            //2. 使用请求参数去请求数据回来并入库
            for (MerchentReq merchentReq : merchentReqs) {
                DataBus dataBus = getOrderByOKHttp(merchentReq);
                res.put(merchentReq.getUuid(), dataBus);
            }

            return DataBus.success(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DataBus.failure();
    }

    /**
     * 获取订单并保存到db
     *
     * @param merchentReq
     * @return
     */
    private DataBus getOrderByOKHttp(MerchentReq merchentReq) throws Exception {
        //cache for new cookie from the request
        Map<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
        //header
        Map<String, String> headers = merchentReq.getHeaders();
        //cookie
        Map<String, String> cookies = merchentReq.getCookies();
        //轮询请求机制，直到请求的数据为空集合的时候停止
        MediaType mediaType = MediaType.parse(merchentReq.getMetaType().getValue());
        //参数
        Map<String, String> args = merchentReq.getArgs();//JSON.toJSONString(json);//"{\"name\":\"金凤\"}";
        //url
        String url = UrlUtil.generateUrl(merchentReq.getProtocol().getValue(), merchentReq.getDomain(), merchentReq.getUrl());
        while (true) {
            //do req
            MyHttpRequestBuilder myHttpRequestBuilder = MyHttpRequestBuilder.createReqInstance();
            myHttpRequestBuilder.addParameters(args);
            myHttpRequestBuilder.addCookies(cookies);
            myHttpRequestBuilder.addHeaders(headers);
            myHttpRequestBuilder.buildGet(url);
            Response response = myHttpRequestBuilder.execute();
            Integer resCode = response.code();
            if (resCode == 200) {
                //保存订单


                return DataBus.success();
            } else if (resCode == 302) {
                return new DataBus(MessageCode2.NEED_REDIRECT, response.header("Location"));
            } else {
                return new DataBus(MessageCode2.UNKNOW_ERROR, response.headers());
            }
        }
    }


    /**
     * 保存上游的订单数据，
     * 注意解析为多个子订单，这代表我们需要有一个解析器来解析为集合
     *
     * @param orderString
     * @return
     */
    public DataBus<Object> saveOrder(String orderString, MerchentReq merchentReq) {
        merchentRespMapper.getByMerchentID(merchentReq.getUuid());
        //获取解析步骤


        //用解析步骤去解析数据为集合


        //保存集合


        return DataBus.failure();
    }

    /**
     * 转换上游数据到下游数据
     *
     * @return
     */
    @Override
    public DataBus<Object> truncateOrders() {

        return DataBus.failure();
    }


    /**
     * 创建下游订单数据
     *
     * @return
     */
    @Override
    public DataBus<Object> createOrders(List<String> merchentIDs) {

        return DataBus.failure();
    }


}
