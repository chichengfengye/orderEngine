package com.ang.reptile.service.impl;

import com.ang.reptile.config.ConfigReader;
import com.ang.reptile.config.HttpConfig;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.model.Page;
import com.ang.reptile.service.OrderService;
import com.ang.reptile.util.DataParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public abstract class GetOrderServiceImpl implements OrderService {
    protected Logger logger = LoggerFactory.getLogger(GetOrderServiceImpl.class);
    protected static HashMap<String, String> cookieMap = new HashMap<>();
    protected HashMap<String, String> headers = new HashMap<>();
    protected String reqUrl = "";
    protected static final int pageSize = 15;

    /**
     * 1. 获取处于激活状态的上游商家
     * 2. 查询对应的req信息
     * 3. 一次使用req查询数据
     * 4. 一次入库
     * 5. 在4中的所有商家的数据入库成功后，把他们的所有数据转换为下游数据存储到下游数据表单中
     * 6. 完成
     * @return
     */
    public void getOrders(){

    }


    @Override
    public DataBus<List<String>> loopData() {
        HttpConfig httpConfig = ConfigReader.getHttpConfig("upstream_http.json");

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
                    allDBItemSize += storeOrderToDB(datas);
                } catch (Exception e) {
                    logger.error("============== 插入数据库失败！=================");
                    e.printStackTrace();
                    return DataBus.failure();
                }
            }
        }


        logger.info("+++++++++++共获取 " + allDataSize + "条数据+++++++++++++");
        logger.info("+++++++++++共插入数据库 " + allDBItemSize + "条记录+++++++++++++");

        DataBus<List<String>> dataBus = DataBus.success();

        return dataBus;
    }

    protected abstract DataBus<String> getDoorTrackingData(Page page);
}
