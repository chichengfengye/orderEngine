package com.ang.reptile.mapper;

import com.ang.reptile.entity.Merchent;

import java.util.List;

public interface MerchentMapper {
    int insert(Merchent merchent);

    Merchent getMerchent(Merchent merchent);

    List<Merchent> getMerchents();
}
