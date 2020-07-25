package com.ang.reptile.mapper;

import com.ang.reptile.entity.MerchentReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MerchentReqMapper {
    List<MerchentReq> getAll();

    List<MerchentReq> getByIds(@Param("ids") List<String> ids);
}
