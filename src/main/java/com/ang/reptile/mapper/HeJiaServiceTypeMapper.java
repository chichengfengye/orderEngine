package com.ang.reptile.mapper;

import com.ang.reptile.model.BangJiaServiceType;
import com.ang.reptile.model.HeJiaServiceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeJiaServiceTypeMapper {
    int insert(HeJiaServiceType serviceType);

    int inserts(@Param("serviceTypes") List<HeJiaServiceType> list);

    List<HeJiaServiceType> getAll();
}
