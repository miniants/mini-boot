package cn.miniants.framework.mybatistype;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.data.geo.Point;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author guoqianyou
 * @date 2020/12/6 13:26
 */
@MappedTypes({Point.class})
public class GeoPointTypeHandler extends BaseTypeHandler<Point> {
    GeoPointConverter converter = new GeoPointConverter();

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Point point, JdbcType jdbcType) throws SQLException {
        preparedStatement.setBytes(i, converter.to(point));
    }

    @Override
    public Point getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return converter.from(resultSet.getBytes(s));
    }

    @Override
    public Point getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return converter.from(resultSet.getBytes(i));
    }

    @Override
    public Point getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return converter.from(callableStatement.getBytes(i));
    }
}
