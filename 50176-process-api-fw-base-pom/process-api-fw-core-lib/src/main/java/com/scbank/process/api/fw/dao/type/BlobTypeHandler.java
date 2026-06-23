package com.scbank.process.api.fw.dao.type;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * <pre>
 * MyBatis용 BLOB ↔ byte[] TypeHandler
 * - DB의 BLOB 필드를 Java byte[]로 매핑
 * - null-safe 및 stream 기반 처리 포함
 * </pre>
 * 
 * @author sungdon.choi
 */
public class BlobTypeHandler extends BaseTypeHandler<byte[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, byte[] parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setBytes(i, parameter);
    }

    @Override
    public byte[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toBytes(rs.getBlob(columnName));
    }

    @Override
    public byte[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toBytes(rs.getBlob(columnIndex));
    }

    @Override
    public byte[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toBytes(cs.getBlob(columnIndex));
    }

    private byte[] toBytes(Blob blob) {
        if (blob == null)
            return null;
        try (InputStream is = blob.getBinaryStream()) {
            return is.readAllBytes(); // Java 11 이상, Java 8은 ByteArrayOutputStream 사용
        } catch (Exception e) {
            throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR, "BLOB 읽기 실패", e);
        }
    }
}
