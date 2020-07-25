package com.ang.reptile.mapper.typehandler;
import com.ang.reptile.Enum.MerchentStatusEnum;
import com.ang.reptile.Enum.MerchentStatusEnum;
import com.ang.reptile.Enum.MyEnumUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MerchentStatusEnumHandler implements TypeHandler<MerchentStatusEnum> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, MerchentStatusEnum eNum, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setInt(i, eNum.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MerchentStatusEnum getResult(ResultSet resultSet, String s) throws SQLException {
        int res = resultSet.getInt(s);
        MerchentStatusEnum eNum = MyEnumUtil.getByCode(res, MerchentStatusEnum.class);
        return eNum;
    }

    @Override
    public MerchentStatusEnum getResult(ResultSet resultSet, int i) throws SQLException {
        int res = resultSet.getInt(i);
        MerchentStatusEnum eNum = MyEnumUtil.getByCode(res, MerchentStatusEnum.class);
        return eNum;
    }

    @Override
    public MerchentStatusEnum getResult(CallableStatement callableStatement, int i) throws SQLException {
        int res = callableStatement.getInt(i);
        MerchentStatusEnum eNum = MyEnumUtil.getByCode(res, MerchentStatusEnum.class);
        return eNum;
    }
}
