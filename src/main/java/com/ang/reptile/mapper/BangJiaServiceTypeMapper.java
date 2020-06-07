package com.ang.reptile.mapper;

import com.ang.reptile.model.BangJiaServiceType;
import com.ang.reptile.model.ServiceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BangJiaServiceTypeMapper {
    int insert(BangJiaServiceType serviceType);

    int inserts(@Param("serviceTypes") List<BangJiaServiceType> list);

    List<BangJiaServiceType> getAll();

//    ServiceType getOne(BangJiaServiceType serviceType);
}
