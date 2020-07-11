package com.ang.reptile.mapper;

import com.ang.reptile.entity.LvDiOrder;

import java.util.List;

public interface LvdiOrderMapper {
    int insert(LvDiOrder order);

    int inserts(List<LvDiOrder> orders);
}
