package com.ang.reptile.mapper.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class MapHandler implements TypeHandler<Map<String, Object>> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Map<String, Object> map, JdbcType jdbcType) throws SQLException {

        try {
            preparedStatement.setString(i, JSONObject.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, String s) throws SQLException {
        Map<String, Object> map = null;
        String res = resultSet.getString(s);
        if (res != null) {
            map = JSON.parseObject(res);
        }
        return map;
    }

    @Override
    public Map<String, Object> getResult(ResultSet resultSet, int i) throws SQLException {
        Map<String, Object> map = null;
        String res = resultSet.getString(i);
        if (res != null) {
            map = JSON.parseObject(res);
        }

        return map;
    }

    @Override
    public Map<String, Object> getResult(CallableStatement callableStatement, int i) throws SQLException {
        Map<String, Object> map = null;
        String res = callableStatement.getString(i);
        if (res != null) {
            map = JSON.parseObject(res);
        }
        return map;
    }
}
