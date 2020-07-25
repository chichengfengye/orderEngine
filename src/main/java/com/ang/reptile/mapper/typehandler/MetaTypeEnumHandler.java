package com.ang.reptile.mapper.typehandler;
import com.ang.reptile.Enum.MetaTypeEnum;
import com.ang.reptile.Enum.MyEnumUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetaTypeEnumHandler implements TypeHandler<MetaTypeEnum> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, MetaTypeEnum eNum, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setInt(i, eNum.getCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public MetaTypeEnum getResult(ResultSet resultSet, String s) throws SQLException {
        int res = resultSet.getInt(s);
        MetaTypeEnum eNum = MyEnumUtil.getByCode(res, MetaTypeEnum.class);
        return eNum;
    }

    @Override
    public MetaTypeEnum getResult(ResultSet resultSet, int i) throws SQLException {
        int res = resultSet.getInt(i);
        MetaTypeEnum eNum = MyEnumUtil.getByCode(res, MetaTypeEnum.class);
        return eNum;
    }

    @Override
    public MetaTypeEnum getResult(CallableStatement callableStatement, int i) throws SQLException {
        int res = callableStatement.getInt(i);
        MetaTypeEnum eNum = MyEnumUtil.getByCode(res, MetaTypeEnum.class);
        return eNum;
    }
}
