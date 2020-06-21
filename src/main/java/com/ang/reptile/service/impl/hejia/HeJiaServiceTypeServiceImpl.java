package com.ang.reptile.service.impl.hejia;

import com.ang.reptile.mapper.HeJiaServiceTypeMapper;
import com.ang.reptile.model.HeJiaServiceType;
import com.ang.reptile.service.HeJiaServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeJiaServiceTypeServiceImpl implements HeJiaServiceTypeService {

    @Autowired
    private HeJiaServiceTypeMapper mapper;

    @Override
    public int insert(HeJiaServiceType serviceType) {
        return mapper.insert(serviceType);
    }

    @Override
    public int inserts(List<HeJiaServiceType> list) {
        return mapper.inserts(list);
    }

    @Override
    public List<HeJiaServiceType> getAll() {
        return mapper.getAll();
    }
}
