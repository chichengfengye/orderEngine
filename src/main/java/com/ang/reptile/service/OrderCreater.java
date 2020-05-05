package com.ang.reptile.service;

import com.ang.reptile.dto.DBQueryPage;
import com.ang.reptile.mapper.HeJiaOrderMapper;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.model.Page;
import com.ang.reptile.pojo.HeJiaOrder;
import okhttp3.FormBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCreater {
    @Autowired
    private HeJiaOrderMapper mapper;

    public DataBus createOrder() {
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

    //发送请求到帮家
    public DataBus postToBangJia(HeJiaOrder heJiaOrder) {
        //todo 创建http客户端
        FormBody.Builder builder = new FormBody.Builder();
//        HashMap<String, String> urlEncodedMap = UrlEncodedUtil.getUrlencodedMap(queryData, HeJiaQueryData.class);
//        if (urlEncodedMap != null && urlEncodedMap.size() >= 0) {
//            for (Map.Entry<String, String> stringObjectEntry : urlEncodedMap.entrySet()) {
//                String name = stringObjectEntry.getKey();
//                String value = stringObjectEntry.getValue();
//                builder.add(name, value);
//            }
//        }

        //发送请求

        //解析结果

        //更新数据库的active字段

        //响应结果

        return DataBus.failure();
    }

}
