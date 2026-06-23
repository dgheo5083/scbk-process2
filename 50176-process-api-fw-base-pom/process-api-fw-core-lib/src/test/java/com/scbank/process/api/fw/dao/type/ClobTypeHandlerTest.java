package com.scbank.process.api.fw.dao.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

/**
 * {@link ClobTypeHandler} 단위 테스트.
 */
class ClobTypeHandlerTest {

    private ClobTypeHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ClobTypeHandler();
    }

    @Test
    @DisplayName("setNonNullParameter 는 PreparedStatement.setCharacterStream 으로 위임")
    void setNonNullParameter() throws Exception {
        PreparedStatement ps = mock(PreparedStatement.class);

        handler.setNonNullParameter(ps, 1, "hello", JdbcType.CLOB);

        verify(ps).setCharacterStream(eq(1), any(Reader.class), eq("hello".length()));
    }

    @Test
    @DisplayName("ResultSet/컬럼명 으로 CLOB 을 String 으로 변환")
    void getNullableResultByColumnName() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Clob clob = mock(Clob.class);
        when(rs.getClob("col")).thenReturn(clob);
        when(clob.getCharacterStream()).thenReturn(new StringReader("clob-value"));

        assertThat(handler.getNullableResult(rs, "col")).isEqualTo("clob-value");
    }

    @Test
    @DisplayName("ResultSet/컬럼인덱스 로 CLOB 을 String 으로 변환")
    void getNullableResultByColumnIndex() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Clob clob = mock(Clob.class);
        when(rs.getClob(2)).thenReturn(clob);
        when(clob.getCharacterStream()).thenReturn(new StringReader("idx-value"));

        assertThat(handler.getNullableResult(rs, 2)).isEqualTo("idx-value");
    }

    @Test
    @DisplayName("CallableStatement 로 CLOB 을 String 으로 변환")
    void getNullableResultFromCallableStatement() throws Exception {
        CallableStatement cs = mock(CallableStatement.class);
        Clob clob = mock(Clob.class);
        when(cs.getClob(anyInt())).thenReturn(clob);
        when(clob.getCharacterStream()).thenReturn(new StringReader("cs-value"));

        assertThat(handler.getNullableResult(cs, 3)).isEqualTo("cs-value");
    }

    @Test
    @DisplayName("2048자 초과 CLOB 도 정상적으로 누적 변환")
    void largeClobIsFullyRead() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Clob clob = mock(Clob.class);
        String large = "x".repeat(5000);
        when(rs.getClob("big")).thenReturn(clob);
        when(clob.getCharacterStream()).thenReturn(new StringReader(large));

        assertThat(handler.getNullableResult(rs, "big")).isEqualTo(large);
    }

    @Test
    @DisplayName("CLOB 이 null 이면 null 반환")
    void nullClobReturnsNull() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getClob("col")).thenReturn(null);

        assertThat(handler.getNullableResult(rs, "col")).isNull();
    }

    @Test
    @DisplayName("CLOB 스트림 읽기 실패 시 FrameworkRuntimeException")
    void readFailureWrappedAsFrameworkException() throws Exception {
        ResultSet rs = mock(ResultSet.class);
        Clob clob = mock(Clob.class);
        Reader failing = mock(Reader.class);
        when(rs.getClob("col")).thenReturn(clob);
        when(clob.getCharacterStream()).thenReturn(failing);
        when(failing.read(any(char[].class))).thenThrow(new IOException("read error"));

        assertThatThrownBy(() -> handler.getNullableResult(rs, "col"))
                .isInstanceOf(FrameworkRuntimeException.class)
                .hasMessageContaining("CLOB");
    }
}
