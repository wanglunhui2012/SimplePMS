package indi.simple.pms.typehandler;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:33
 * @Description:
 */
@MappedJdbcTypes({JdbcType.VARCHAR})
@MappedTypes({List.class})
@Slf4j
public class ListLongJacksonTypeHandler extends BaseTypeHandler<Object> {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Class<List> type;

    public ListLongJacksonTypeHandler(Class<List> type) {
        if (log.isTraceEnabled()) {
            log.trace("JacksonTypeHandler(" + type + ")");
        }

        if (null == type) {
            throw new MybatisPlusException("Type argument cannot be null");
        } else {
            this.type = type;
        }
    }

    private Object parse(String json) {
        try {
            return json != null && json.length() != 0 ? objectMapper.readValue(json, new TypeReference<List<Long>>() {
            }) : null;
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.parse(rs.getString(columnName));
    }

    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.parse(rs.getString(columnIndex));
    }

    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.parse(cs.getString(columnIndex));
    }

    public void setNonNullParameter(PreparedStatement ps, int columnIndex, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(columnIndex, this.toJsonString(parameter));
    }
}
