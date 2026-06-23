package com.scbank.process.api.fw.dao.type;

import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * <pre>
 * MyBatis용 CLOB ↔ String TypeHandler
 * - DB의 CLOB 필드를 Java String으로 매핑
 * - null-safe 처리 포함
 * 
 * 사용 시 mapper에 typeHandler="co.kr.scbank.framework.dao.type.ClobTypeHandler"
 * </pre>
 * 
 * @author sungdon.choi
 */
public class ClobTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setCharacterStream(i, new java.io.StringReader(parameter), parameter.length());
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Clob clob = rs.getClob(columnName);
        return toString(clob);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Clob clob = rs.getClob(columnIndex);
        return toString(clob);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Clob clob = cs.getClob(columnIndex);
        return toString(clob);
    }

    private String toString(Clob clob) {
        if (clob == null)
            return null;
        try (Reader reader = clob.getCharacterStream()) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[2048];
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR, "CLOB 읽기 실패", e);
        }
    }
}
