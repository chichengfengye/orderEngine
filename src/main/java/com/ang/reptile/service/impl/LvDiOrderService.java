package com.ang.reptile.service.impl;

import com.ang.reptile.model.DataBus;
import com.ang.reptile.service.GetOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("lvDiOrderService")
public class LvDiOrderService implements GetOrderService {
    @Override
    public DataBus<List<String>> loopDoorTrackingData() {
        return null;
    }

    @Override
    public int insertSmOrderByString(List<String> datas) throws Exception {
        return 0;
    }
}
