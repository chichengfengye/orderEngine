package com.ang.reptile.service.impl;

import com.ang.reptile.Enum.BangJiaOrderStateEnum;
import com.ang.reptile.code.MessageCode;
import com.ang.reptile.dto.DBQueryPage;
import com.ang.reptile.mapper.BangJiaOrderMapper;
import com.ang.reptile.mapper.HeJiaOrderMapper;
import com.ang.reptile.model.Address;
import com.ang.reptile.model.DataBus;
import com.ang.reptile.entity.BangJiaOrder;
import com.ang.reptile.entity.HeJiaOrder;
import com.ang.reptile.entity.ItemList;
import com.ang.reptile.service.TruncateOrderService;
import com.ang.reptile.util.BangJiaOrderUtil;
import com.ang.reptile.util.CityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TruncateOrderServiceBangJia implements TruncateOrderService {
    private Logger logger = LoggerFactory.getLogger(TruncateOrderServiceBangJia.class);
    private SimpleDateFormat repairDateFormat = new SimpleDateFormat("yy:MM:dd HH:mm:ss");
    private SimpleDateFormat buyDateFormat = new SimpleDateFormat("yy-MM-dd");

    @Autowired
    private HeJiaOrderMapper heJiaOrderMapper;
    @Autowired
    private BangJiaOrderMapper bangJiaOrderMapper;
    @Autowired
    private OrderTypeServiceImpl orderTypeServiceImpl;

    /**
     * 在获取到的阖家订单中，
     * 将可以转换的转换为帮家订单
     * 并 入帮家订单表，帮家订单的state为wait
     */
    @Override
    public void truncateOrder() {
        DBQueryPage page = new DBQueryPage();
        page.setPageSize(30);

        long successNum = 0L;
        long totalOrder = 0l;
        long id = 0L;
        //轮循获取数据
        while (true) {
            page.setId(id);
            List<HeJiaOrder> hejiaOrders = heJiaOrderMapper.getHeJiaOrders(page, true);
            if (hejiaOrders == null || hejiaOrders.size() == 0) {
                if (totalOrder == 0l) {
                    logger.info("没有合家订单用于转换！");
                }
                break;
            } else {
                id = hejiaOrders.get(hejiaOrders.size() - 1).getId();
                for (HeJiaOrder hejiaOrder : hejiaOrders) {
                    totalOrder++;
                    //解析
                    DataBus<List<BangJiaOrder>> result = truncateHe2Bang(hejiaOrder);
                    if (result.getCode() != DataBus.SUCCESS_CODE) {
                        logger.error(result.getMsg());
                        logger.info("由于解析订单出错, 退出程序");
                        return;
                    } else {
                        //入库
                        bangJiaOrderMapper.inserts(result.getData());
                        hejiaOrder.setActive(false);
                        heJiaOrderMapper.updateStateById(hejiaOrder);
                        successNum++;
                    }
                }            }
        }

        //日志反馈
        logger.info("=====> 本次查询需要转换的合家订单一共有{}个",totalOrder);
        logger.info("=====> 转换为帮家订单,成功{}个，失败{}个", successNum, totalOrder - successNum);

    }

    /**
     * 将一个合家订单转换为帮家订单列表（因为一个合家订单能创建帮家订单的数量>=1）
     * @param heJiaOrder
     * @return
     */
    private DataBus<List<BangJiaOrder>> truncateHe2Bang(HeJiaOrder heJiaOrder) {
        List<BangJiaOrder> orders = new ArrayList<>();

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
        Date repaireDate = BangJiaOrderUtil.getRepairedDate(buyDate);//repaireDateCalender.getTime();
        //todo bug
        String orderType = orderTypeServiceImpl.getServiceCode(orderInfoName);//;// //服务项目
        Address addressConfig = CityUtil.getAddress(address);
        if (addressConfig == null) {
            logger.error("===>找不到address【{}】的配置省市地址！", address);
            return DataBus.failure(MessageCode.Code.ADDRESS_NOT_MATCHED, MessageCode.Message.ADDRESS_NOT_MATCHED);
        }
        String province = addressConfig.getProvince().getCode();
        String city = addressConfig.getCity().getCode();
        String county = addressConfig.getCounty().getCode();
        String town = addressConfig.getTown().getCode();
        String note = "订单名称：" + orderInfoName + "\n； 所属合家订单编码:" + code;

        for (ItemList itemList : itemLists) {
            Double amount = itemList.getAmount();
            String commodityInfoName = itemList.getCommodityInfoName();
            Integer commodityInfoNum = itemList.getCommodityInfoNum();
            Double commodityInfoPrice = itemList.getCommodityInfoPrice();
            String skucode = itemList.getSkucode();
            String summary = itemList.getSummary();

            BangJiaOrder bangJiaOrder = new BangJiaOrder();
            bangJiaOrder.setHejiaOrderCode(code);
            bangJiaOrder.setNumber_0(commodityInfoNum);
            bangJiaOrder.setOrdertype(orderType);
            bangJiaOrder.setServicemode("上门");
            bangJiaOrder.setGuarantee(1);
            bangJiaOrder.setOriginname(addressConfig.getVillage());//工单来源 指的是哪个小区来的工单--2020年5月17日
            bangJiaOrder.setFactorynumber(orderInfoName);
            bangJiaOrder.setPrice(0d);
            bangJiaOrder.setRepairdate(repairDateFormat.format(repaireDate));
            bangJiaOrder.setUsername(userName);
            bangJiaOrder.setMobile(mobile);
            bangJiaOrder.setProvince(province);
            bangJiaOrder.setCity(city);
            bangJiaOrder.setCounty(county);
            bangJiaOrder.setTown(town);
            bangJiaOrder.setAddress(address);
            bangJiaOrder.setMbuyprice(amount);
            bangJiaOrder.setCreatename("靳丰");
            bangJiaOrder.setMbuydate(buyDateFormat.format(buyDate));
            bangJiaOrder.setNote(note);
            bangJiaOrder.setState(BangJiaOrderStateEnum.WAIT);

            orders.add(bangJiaOrder);
        }


        return DataBus.success(orders);
    }


}
