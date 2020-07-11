package com.ang.reptile.mapper;

import com.ang.reptile.entity.MerchentReq;

import java.util.List;

public interface MerchentReqMapper {
    List<MerchentReq> getAll();

    List<MerchentReq> getByIds(List<String> merchentIDs);
}
