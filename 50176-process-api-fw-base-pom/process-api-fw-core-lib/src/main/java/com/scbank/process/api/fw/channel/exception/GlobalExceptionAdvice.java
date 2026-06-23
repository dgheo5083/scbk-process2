package com.scbank.process.api.fw.channel.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.scbank.process.api.fw.channel.message.IResponseMessage;
import com.scbank.process.api.fw.channel.message.IResponseMessageFactory;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.runtime.RuntimeContext;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

/**
 * 전역 예외처리 RestControllerAdvice
 *
 * @author sungdon.choi
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(prefix = "csl.channel.exception", name = "handle-global-advice", havingValue = "true")
public class GlobalExceptionAdvice {

    /**
     * 응답 메시지 팩토리
     */
    @SuppressWarnings("rawtypes")
    private IResponseMessageFactory responseMessageFactory;

    @SuppressWarnings("rawtypes")
    private IResponseMessageFactory getResponseMessageFactory() {
        return RuntimeContext.getBean(IResponseMessageFactory.class);
    }

    /**
     * 입력 DTO 유효성 검증 처리 오류 핸들링
     *
     * @param ex {@link ConstraintViolationException}
     * @return ResponseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public <H, T> ResponseEntity<?> handleValidationException(ConstraintViolationException ex) {
        this.traceLog(ex);
        this.responseMessageFactory = this.getResponseMessageFactory();
        return ResponseEntity.badRequest().body(this.responseMessageFactory.fail(ex));
    }

    /**
     * 전체 오류에 대한 핸들링
     *
     * @param ex  {@link Throwable}
     * @param <H> 응답 헤더
     * @param <T> 응답 바디
     * @return ResponseEntity @{@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    @ExceptionHandler(Throwable.class)
    public <H, T> ResponseEntity<IResponseMessage<H, T>> handleThrowable(Throwable ex) {
        log.error(ex.getMessage(), ex);
        this.traceLog(ex);
        this.responseMessageFactory = this.getResponseMessageFactory();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(this.responseMessageFactory.fail(ex));
    }

    /**
     * 트레이스 로그 처리
     * 
     * @param ex {@link Throwable}
     */
    private void traceLog(Throwable ex) {
        TraceContextHolder.get().ifPresent((traceContext) -> traceContext.fail(ex));
    }
}
