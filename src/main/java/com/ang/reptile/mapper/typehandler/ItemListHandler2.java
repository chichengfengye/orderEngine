package com.ang.reptile.mapper.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ang.reptile.pojo.ItemList;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ItemListHandler2 implements TypeHandler<ItemList> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, ItemList itemList, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i, JSONObject.toJSONString(itemList));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemList getResult(ResultSet resultSet, String s) throws SQLException {
        ItemList lists = null;
        String res = resultSet.getString(s);
        if (res != null) {
            lists = JSON.parseObject(res, ItemList.class);
        }

        return lists;
    }

    @Override
    public ItemList getResult(ResultSet resultSet, int i) throws SQLException {
        ItemList lists = null;
        String res = resultSet.getString(i);
        if (res != null) {
            lists = JSON.parseObject(res, ItemList.class);
        }

        return lists;
    }

    @Override
    public ItemList getResult(CallableStatement callableStatement, int i) throws SQLException {
        ItemList lists = null;
        String res = callableStatement.getString(i);
        if (res != null) {
            lists = JSON.parseObject(res, ItemList.class);
        }

        return lists;
    }
}
