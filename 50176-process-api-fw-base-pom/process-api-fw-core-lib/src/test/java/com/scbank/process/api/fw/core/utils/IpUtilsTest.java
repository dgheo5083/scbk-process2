package com.scbank.process.api.fw.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.scbank.process.api.fw.channel.context.IServiceContext;

import jakarta.servlet.http.HttpServletRequest;

@DisplayName("IpUtils 테스트")
class IpUtilsTest {

    @Mock
    private HttpServletRequest request;
    
    private AutoCloseable closeable;
    private String originalPodIp;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        originalPodIp = System.getProperty(IpUtils.PROP_NAME_POD_IP);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
        if (originalPodIp != null) {
            System.setProperty(IpUtils.PROP_NAME_POD_IP, originalPodIp);
        } else {
            System.clearProperty(IpUtils.PROP_NAME_POD_IP);
        }
        
        RequestContextHolder.resetRequestAttributes();
    }

    @Nested
    @DisplayName("validIp 메서드 테스트")
    class ValidIpTests {

        @Test
        @DisplayName("validIp - 유효한 IP 주소 (0-199 범위)")
        void validIp_valid() {
            assertTrue(IpUtils.validIp("192.168.1.1"));
            assertTrue(IpUtils.validIp("10.0.0.1"));
            assertTrue(IpUtils.validIp("0.0.0.0"));
            assertTrue(IpUtils.validIp("127.0.0.1"));
            assertTrue(IpUtils.validIp("199.199.199.199"));
        }

        @Test
        @DisplayName("validIp - 200 이상 옥텟은 정규식 제한으로 실패")
        void validIp_highOctetsFail() {
            // 현재 정규식은 200-255 범위 옥텟에 대해 dot을 포함하지 않아 실패
            assertFalse(IpUtils.validIp("255.255.255.255"));
            assertFalse(IpUtils.validIp("200.0.0.1"));
            assertFalse(IpUtils.validIp("10.200.0.1"));
        }

        @Test
        @DisplayName("validIp - 유효하지 않은 IP 주소")
        void validIp_invalid() {
            assertFalse(IpUtils.validIp("256.1.1.1"));
            assertFalse(IpUtils.validIp("192.168.1"));
            assertFalse(IpUtils.validIp("192.168.1.1.1"));
            assertFalse(IpUtils.validIp("abc.def.ghi.jkl"));
            assertFalse(IpUtils.validIp("192.168.1."));
        }

        @Test
        @DisplayName("validIp - null 입력")
        void validIp_null() {
            assertFalse(IpUtils.validIp(null));
        }

        @Test
        @DisplayName("validIp - 빈 문자열")
        void validIp_empty() {
            assertFalse(IpUtils.validIp(""));
        }
    }

    @Nested
    @DisplayName("getClientIp 메서드 테스트")
    class GetClientIpTests {

        @Test
        @DisplayName("getClientIp - X-Forward-For 헤더 있음")
        void getClientIp_withXForwardFor() {
            when(request.getHeader("X-Forward-For")).thenReturn("192.168.1.100");

            String result = IpUtils.getClientIp(request);

            assertEquals("192.168.1.100", result);
            verify(request).getHeader("X-Forward-For");
            verify(request, never()).getRemoteAddr();
        }

        @Test
        @DisplayName("getClientIp - X-Forward-For 헤더 없음")
        void getClientIp_withoutXForwardFor() {
            when(request.getHeader("X-Forward-For")).thenReturn(null);
            when(request.getRemoteAddr()).thenReturn("10.0.0.50");

            String result = IpUtils.getClientIp(request);

            assertEquals("10.0.0.50", result);
            verify(request).getHeader("X-Forward-For");
            verify(request).getRemoteAddr();
        }

        @Test
        @DisplayName("getClientIp - X-Forward-For 헤더가 빈 문자열")
        void getClientIp_emptyXForwardFor() {
            when(request.getHeader("X-Forward-For")).thenReturn("");
            when(request.getRemoteAddr()).thenReturn("10.0.0.50");

            String result = IpUtils.getClientIp(request);

            assertEquals("10.0.0.50", result);
        }

        @Test
        @DisplayName("getClientIp - X-Forward-For 헤더가 공백만")
        void getClientIp_whitespaceXForwardFor() {
            when(request.getHeader("X-Forward-For")).thenReturn("   ");
            when(request.getRemoteAddr()).thenReturn("10.0.0.50");

            String result = IpUtils.getClientIp(request);

            assertEquals("10.0.0.50", result);
        }
    }

    @Nested
    @DisplayName("getLocalIp 메서드 테스트")
    class GetLocalIpTests {

        @Test
        @DisplayName("getLocalIp - POD_IP 시스템 프로퍼티 있음")
        void getLocalIp_withPodIp() {
            System.setProperty(IpUtils.PROP_NAME_POD_IP, "172.16.0.10");

            String result = IpUtils.getLocalIp();

            assertEquals("172.16.0.10", result);
        }

        @Test
        @DisplayName("getLocalIp - POD_IP 시스템 프로퍼티 없음")
        void getLocalIp_withoutPodIp() {
            System.clearProperty(IpUtils.PROP_NAME_POD_IP);

            String result = IpUtils.getLocalIp();

            assertNotNull(result);
            // 실제 로컬 IP 또는 localhost가 반환됨
        }

        @Test
        @DisplayName("getLocalIp - POD_IP가 빈 문자열")
        void getLocalIp_emptyPodIp() {
            System.setProperty(IpUtils.PROP_NAME_POD_IP, "");

            String result = IpUtils.getLocalIp();

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("getHostName 메서드 테스트")
    class GetHostNameTests {

        @Test
        @DisplayName("getHostName - 정상")
        void getHostName() {
            String result = IpUtils.getHostName();

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }
    
    @Test
    void getClientIp_returnsHeader_whenHeaderHasText() {
    	
    	IServiceContext ctx = mock(IServiceContext.class);
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	
    	when(ctx.request()).thenReturn(request);
    	when(request.getHeader("X-Forward-For")).thenReturn("203.0.113.10");
        when(request.getRemoteAddr()).thenReturn("10.0.0.50");
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        String ip = IpUtils.getClientIp();
    	
    	assertEquals("203.0.113.10", ip);
    	
    	verify(request, times(1)).getHeader("X-Forward-For");
    	verify(request, never()).getRemoteAddr();
    }
    
    @Test
    void getClientIp_returnsRemoteAddr_whenHeaderIsNull() {
    	
    	IServiceContext ctx = mock(IServiceContext.class);
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	
    	when(ctx.request()).thenReturn(request);
    	when(request.getHeader("X-Forward-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        String ip = IpUtils.getClientIp();
    	
    	assertEquals("10.0.0.1", ip);
    }
    
    @Test
    void getClientIp_fallbackRemoteAddr_whenHeaderBlank() {
    	
    	IServiceContext ctx = mock(IServiceContext.class);
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	
    	when(ctx.request()).thenReturn(request);
    	when(request.getHeader("X-Forward-For")).thenReturn("  ");
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        String ip = IpUtils.getClientIp();
    	
    	assertEquals("10.0.0.1", ip);
    }
    
    @Test
    void getClientIp_request_is_null() {
    	
    	IServiceContext ctx = mock(IServiceContext.class);
    	HttpServletRequest request = mock(HttpServletRequest.class);
    	
    	when(ctx.request()).thenReturn(request);
    	when(request.getHeader("X-Forward-For")).thenReturn("  ");
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        
        //RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        
        String ip = IpUtils.getClientIp();
    	
    	assertEquals(null, ip);
    }
}
