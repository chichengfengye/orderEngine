package com.ang.reptile;

import com.ang.reptile.service.GetOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class ReptileApplicationRunner implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(ReptileApplicationRunner.class);

    @Autowired
    private GetOrderService getOrderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*try {
            logger.info("开始抓取数据！请等待返回结果！");

            DataBus<List<String>> dataBus = doorTracking.loopDoorTrackingData();
            if (dataBus.getCode() == DataBus.SUCCESS_CODE) {
                logger.info("获取数据结束！退出程序！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取数据出现异常！退出程序！");
        }*/
    }
}
