package com.scbank.process.api.fw.base.store.secure;

import java.util.Objects;
import java.util.Optional;

import com.scbank.process.api.fw.base.store.secure.vo.SecureContext;

/**
 * <pre>
 * 공통 보안영역 SecureContext 저장소
 * 
 * - ThreadLocal 기반: 요청(스레드) 단위로 SecureContext를 보관
 * - 반드시 요청 처리 완료 시 clearContext() 호출 권장
 * </pre>
 * 
 * @author sungdon.choi
 */
public class SecureContextStore {

    private static final ThreadLocal<SecureContext> ctx = new ThreadLocal<>();
    
    private SecureContextStore() {
    	
    }

    /**
     * 현재 스레드의 SecureContext 조회
     * 
     * @return SecureContext
     */
    public static Optional<SecureContext> getContext() {
        return Optional.ofNullable(ctx.get());
    }

    /**
     * 현재스레드에 SecureContext 설정
     * 
     * @param context SecureContext
     */
    public static void setContext(SecureContext context) {
        Objects.requireNonNull(ctx, "ctx must not be null");
        ctx.set(context);
    }

    /**
     * 현재 스레드의 SecureContext 제거
     */
    public static void clearContext() {
        ctx.remove();
    }
}
