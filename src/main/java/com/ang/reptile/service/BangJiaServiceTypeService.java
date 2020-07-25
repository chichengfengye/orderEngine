package com.ang.reptile.service;

import com.ang.reptile.entity.BangJiaServiceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BangJiaServiceTypeService {
    int insert(BangJiaServiceType serviceType);

    int inserts(@Param("serviceTypes") List<BangJiaServiceType> list);

    List<BangJiaServiceType> getAll();
}
