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

public class ItemListHandler implements TypeHandler<List<ItemList>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<ItemList> itemLists, JdbcType jdbcType) throws SQLException {

        try {
            preparedStatement.setString(i, JSONObject.toJSONString(itemLists));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ItemList> getResult(ResultSet resultSet, String s) throws SQLException {
        List<ItemList> lists = null;
        String res = resultSet.getString(s);
        if (res != null) {
            lists = JSON.parseArray(res, ItemList.class);
        }

        return lists;
    }

    @Override
    public List<ItemList> getResult(ResultSet resultSet, int i) throws SQLException {
        List<ItemList> lists = null;
        String res = resultSet.getString(i);
        if (res != null) {
            lists = JSON.parseArray(res, ItemList.class);
        }

        return lists;
    }

    @Override
    public List<ItemList> getResult(CallableStatement callableStatement, int i) throws SQLException {
        List<ItemList> lists = null;
        String res = callableStatement.getString(i);
        if (res != null) {
            lists = JSON.parseArray(res, ItemList.class);
        }
        return lists;
    }
}
