package com.scbank.process.api.fw.dao.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * {@link BlobTypeHandler} 단위 테스트.
 */
class BlobTypeHandlerTest {

    private BlobTypeHandler handler;

    @BeforeEach
    void setUp() {
        handler = new BlobTypeHandler();
    }

    @Test
    @DisplayName("setNonNullParameter 는 PreparedStatement.setBytes 로 위임")
    void setNonNullParameter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);
        byte[] data = { 1, 2, 3 };

        handler.setNonNullParameter(ps, 1, data, JdbcType.BLOB);

        verify(ps).setBytes(1, data);
    }

    @Test
    @DisplayName("ResultSet/컬럼명 으로 BLOB 을 byte[] 로 변환")
    void getNullableResultByColumnName() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Blob blob = mock(Blob.class);
        byte[] expected = { 10, 20, 30 };
        when(rs.getBlob("col")).thenReturn(blob);
        when(blob.getBinaryStream()).thenReturn(new ByteArrayInputStream(expected));

        byte[] result = handler.getNullableResult(rs, "col");

        assertThat(result).containsExactly(expected);
    }

    @Test
    @DisplayName("ResultSet/컬럼인덱스 로 BLOB 을 byte[] 로 변환")
    void getNullableResultByColumnIndex() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Blob blob = mock(Blob.class);
        byte[] expected = { 4, 5 };
        when(rs.getBlob(2)).thenReturn(blob);
        when(blob.getBinaryStream()).thenReturn(new ByteArrayInputStream(expected));

        byte[] result = handler.getNullableResult(rs, 2);

        assertThat(result).containsExactly(expected);
    }

    @Test
    @DisplayName("CallableStatement 로 BLOB 을 byte[] 로 변환")
    void getNullableResultFromCallableStatement() throws Exception {
        CallableStatement cs = mock(CallableStatement.class);
        Blob blob = mock(Blob.class);
        byte[] expected = { 7 };
        when(cs.getBlob(3)).thenReturn(blob);
        when(blob.getBinaryStream()).thenReturn(new ByteArrayInputStream(expected));

        byte[] result = handler.getNullableResult(cs, 3);

        assertThat(result).containsExactly(expected);
    }

    @Test
    @DisplayName("BLOB 이 null 이면 null 반환")
    void nullBlobReturnsNull() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getBlob("col")).thenReturn(null);

        assertThat(handler.getNullableResult(rs, "col")).isNull();
    }

    @Test
    @DisplayName("BLOB 스트림 읽기 실패 시 FrameworkRuntimeException")
    void readFailureWrappedAsFrameworkException() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Blob blob = mock(Blob.class);
        InputStream failing = mock(InputStream.class);
        when(rs.getBlob(eq("col"))).thenReturn(blob);
        when(blob.getBinaryStream()).thenReturn(failing);
        when(failing.readAllBytes()).thenThrow(new IOException("read error"));

        assertThatThrownBy(() -> handler.getNullableResult(rs, "col"))
                .isInstanceOf(FrameworkRuntimeException.class)
                .hasMessageContaining("BLOB");
    }

    @Test
    @DisplayName("anyInt 매처로 컬럼 인덱스 변환 정상 동작")
    void getNullableResultByAnyIndex() throws Exception {
        CallableStatement cs = mock(CallableStatement.class);
        Blob blob = mock(Blob.class);
        when(cs.getBlob(anyInt())).thenReturn(blob);
        when(blob.getBinaryStream()).thenReturn(new ByteArrayInputStream(new byte[] { 1 }));

        assertThat(handler.getNullableResult(cs, 99)).containsExactly((byte) 1);
    }
}
