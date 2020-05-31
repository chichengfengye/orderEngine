package com.ang.reptile.mapper.typehandler;

import com.ang.reptile.Enum.BangJiaOrderStateEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BangJiaOrderStateEnumHandler implements TypeHandler<BangJiaOrderStateEnum> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, BangJiaOrderStateEnum eNum, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setInt(i, eNum.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BangJiaOrderStateEnum getResult(ResultSet resultSet, String s) throws SQLException {
        int res = resultSet.getInt(s);
        BangJiaOrderStateEnum eNum = BangJiaOrderStateEnum.valueOf(res);

        return eNum;
    }

    @Override
    public BangJiaOrderStateEnum getResult(ResultSet resultSet, int i) throws SQLException {
        int res = resultSet.getInt(i);
        BangJiaOrderStateEnum eNum = BangJiaOrderStateEnum.valueOf(res);
        return eNum;
    }

    @Override
    public BangJiaOrderStateEnum getResult(CallableStatement callableStatement, int i) throws SQLException {
        int res = callableStatement.getInt(i);
        BangJiaOrderStateEnum eNum = BangJiaOrderStateEnum.valueOf(res);
        return eNum;
    }
}
