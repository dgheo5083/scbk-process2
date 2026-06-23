package com.scbank.process.api.fw.dao.tx;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.lang.Nullable;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * 프로그래밍 방식(Spring TransactionManager 기반) 트랜잭션 유틸리티 클래스
 * 
 * - @Transactional 을 사용하지 못하는 영역에서 사용
 * - commit / rollback 누락 방지를 위한 공통 처리 제공
 * - 다중 TransactionManager 환경에서 호출부가 transactionManager 를 명시하도록 설계
 * </pre>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TxUtils {

    /**
     * 기본 트랜잭션 정의
     * 
     * @param txManager 사용 대상 transactionManager
     * @return TransactionStatus
     */
    public static TransactionStatus getTransaction(PlatformTransactionManager txManager) {
        return getTransaction(txManager, defaultDefinition(null));
    }

    /**
     * 트랜잭션 이름을 지정하여 트랜잭션 시작
     * 
     * @param txManager 사용 대상 transactionManager
     * @param name      트랜잭션 이름
     * @return TransactionStatus
     */
    public static TransactionStatus getTransaction(PlatformTransactionManager txManager,
            @Nullable String name) {
        return getTransaction(txManager, defaultDefinition(name));
    }

    /**
     * TransactionDefinition 을 직접 받아 트랜잭션 시작
     * 
     * @param txManager  사용 대상 transactionManager
     * @param definition 트랜잭션 정의
     * @return TransactionStatus
     */
    public static TransactionStatus getTransaction(PlatformTransactionManager txManager,
            TransactionDefinition definition) {
        Objects.requireNonNull(txManager, "transactionManager must not be null");
        Objects.requireNonNull(definition, "definition must not be null");
        return txManager.getTransaction(definition);
    }

    /**
     * 트랜잭션 커밋
     * 
     * @param txManager c
     * @param status    TransactionStatus
     */
    public static void commit(PlatformTransactionManager txManager, TransactionStatus status) {
        Objects.requireNonNull(txManager, "transactionManager must not be null");
        Objects.requireNonNull(status, "status must not be null");

        if (!status.isCompleted()) {
            txManager.commit(status);
        }
    }

    /**
     * 트랜잭션 롤백
     * 
     * @param txManager 사용 대상 transactionManager
     * @param status    TransactionStatus
     */
    public static void rollback(PlatformTransactionManager txManager, TransactionStatus status) {
        Objects.requireNonNull(txManager, "transactionManager must not be null");
        Objects.requireNonNull(status, "status must not be null");

        if (!status.isCompleted()) {
            txManager.rollback(status);
        }
    }

    /**
     * 트랜잭션 범위를 람다로 감싸 자동 commit /rollback
     * 
     * @param txManager 사용 대상 transactionManager
     * @param work      트랜잭션 내 실행 로직
     */
    public static void execute(PlatformTransactionManager txManager, Consumer<TransactionStatus> work) {
        execute(txManager, defaultDefinition(null), status -> {
            work.accept(status);
            return null;
        });
    }

    /**
     * 트랜잭션 범위를 람다로 감싸 자동 commit /rollback
     * 
     * @param txManager 사용 대상 transactionManager
     * @param name      트랜잭션 명
     * @param work      트랜잭션 내 실행 로직
     */
    public static void execute(PlatformTransactionManager txManager, @Nullable String name,
            Consumer<TransactionStatus> work) {
        execute(txManager, defaultDefinition(name), status -> {
            work.accept(status);
            return null;
        });
    }

    /**
     * 결과값을 반환하는 트랜잭션 처리 메소드
     * 
     * @param <T>       T 타입
     * @param txManager 사용 대상 transactionManager
     * @param work      트랜잭션 내 실행 로직
     * @return T 타입의 결과값
     */
    public static <T> T executeWithResult(PlatformTransactionManager txManager,
            Function<TransactionStatus, T> work) {
        return execute(txManager, defaultDefinition(null), work);
    }

    /**
     * 결과값을 반환하는 트랜잭션 처리 메소드
     * 
     * @param <T>       T 타입
     * @param name      트랜잭션 명
     * @param txManager 사용 대상 transactionManager
     * @param work      트랜잭션 내 실행 로직
     * @return T 타입의 결과값
     */
    public static <T> T executeWithResult(PlatformTransactionManager txManager, @Nullable String name,
            Function<TransactionStatus, T> work) {
        return execute(txManager, defaultDefinition(name), work);
    }

    /**
     * 결과값을 반환하는 트랜잭션 처리 메소드
     * 
     * @param <T>        T 타입
     * @param txManager  사용 대상 transactionManager
     * @param definition 트랜잭션 정의
     * @param work       트랜잭션 내 실행 로직
     * @return T 타입의 결과값
     */
    public static <T> T execute(PlatformTransactionManager txManager, TransactionDefinition definition,
            Function<TransactionStatus, T> work) {
        Objects.requireNonNull(txManager, "transactionManager must not be null");
        Objects.requireNonNull(definition, "definition must not be null");
        Objects.requireNonNull(work, "work must not be null");

        TransactionStatus status = txManager.getTransaction(definition);
        try {
            // 메소드 실행
            T result = work.apply(status);

            // 커밋
            commit(txManager, status);

            return result;
        } catch (RuntimeException | Error e) {
            // 롤백 처리
            rollback(txManager, status);
            throw e;
        } catch (Exception e) {
            // 롤백 처리
            rollback(txManager, status);
            throw new TxWorkException(FrameworkErrorCode.INTERNAL_ERROR.getCode(), "transaction work fail", e);
        }
    }

    /**
     * 기본 TransactionDefinition 생성
     * 
     * @param name 트랜잭션 명
     * @return DefaultTransactionDefinition
     */
    public static DefaultTransactionDefinition defaultDefinition(@Nullable String name) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        if (name != null && !name.isBlank()) {
            def.setName(name);
        }
        return def;
    }

    /**
     * TransactionDefinition 커스터마이징 헬퍼
     * 
     * @param customizer 트랜잭션 정의 커스터마이저
     * @return DefaultTransactionDefinition
     */
    public static DefaultTransactionDefinition newDefinition(Consumer<DefaultTransactionDefinition> customizer) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        if (customizer != null) {
            customizer.accept(def);
        }
        return def;
    }

    /**
     * 트랜잭션 처리 실행 시 발생한 예외 Wrapping 예외 클래스
     */
    public static class TxWorkException extends FrameworkRuntimeException {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 생성자
		 * @param errorCode 에러코드
		 * @param message 에러메시지
		 * @param cause Throwable
		 */
		public TxWorkException(String errorCode, String message, Throwable cause) {
            super(errorCode, message, cause);
        }
    }
}
