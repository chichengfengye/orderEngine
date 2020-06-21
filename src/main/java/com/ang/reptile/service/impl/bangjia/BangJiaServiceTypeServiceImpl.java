package com.ang.reptile.service.impl.bangjia;

import com.ang.reptile.mapper.BangJiaServiceTypeMapper;
import com.ang.reptile.model.BangJiaServiceType;
import com.ang.reptile.service.BangJiaServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BangJiaServiceTypeServiceImpl implements BangJiaServiceTypeService {
    @Autowired
    private BangJiaServiceTypeMapper mapper;

    @Override
    public int insert(BangJiaServiceType serviceType) {
        return mapper.insert(serviceType);
    }

    @Override
    public int inserts(List<BangJiaServiceType> list) {
        return mapper.inserts(list);
    }

    @Override
    public List<BangJiaServiceType> getAll() {
        return mapper.getAll();
    }
}
