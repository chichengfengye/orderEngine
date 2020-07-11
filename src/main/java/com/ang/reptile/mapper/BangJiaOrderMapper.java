package com.ang.reptile.mapper;

import com.ang.reptile.Enum.BangJiaOrderStateEnum;
import com.ang.reptile.model.Page;
import com.ang.reptile.entity.BangJiaOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BangJiaOrderMapper {
    int insert(BangJiaOrder order);

    List<BangJiaOrder> processGetOrders(@Param("page") Page page, @Param("state") BangJiaOrderStateEnum state);

    int inserts(@Param("bangJiaOrders") List<BangJiaOrder> bangJiaOrders);

    int updateStateById(BangJiaOrder bangJiaOrder);

    int updateStateByIds(@Param("ids") List<Long> ids, @Param("state") BangJiaOrderStateEnum stateEnum);
}
