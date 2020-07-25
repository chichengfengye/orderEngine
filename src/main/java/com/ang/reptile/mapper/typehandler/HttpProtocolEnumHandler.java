package com.ang.reptile.mapper.typehandler;
import com.ang.reptile.Enum.HttpProtocolEnum;
import com.ang.reptile.Enum.MyEnumUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HttpProtocolEnumHandler implements TypeHandler<HttpProtocolEnum> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, HttpProtocolEnum eNum, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setInt(i, eNum.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public HttpProtocolEnum getResult(ResultSet resultSet, String s) throws SQLException {
        int res = resultSet.getInt(s);
        HttpProtocolEnum eNum = MyEnumUtil.getByCode(res, HttpProtocolEnum.class);
        return eNum;
    }

    @Override
    public HttpProtocolEnum getResult(ResultSet resultSet, int i) throws SQLException {
        int res = resultSet.getInt(i);
        HttpProtocolEnum eNum = MyEnumUtil.getByCode(res, HttpProtocolEnum.class);
        return eNum;
    }

    @Override
    public HttpProtocolEnum getResult(CallableStatement callableStatement, int i) throws SQLException {
        int res = callableStatement.getInt(i);
        HttpProtocolEnum eNum = MyEnumUtil.getByCode(res, HttpProtocolEnum.class);
        return eNum;
    }
}
