package com.scbank.process.api.fw.dao.tx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;

/**
 * {@link TxUtils} 단위 테스트.
 */
@ExtendWith(MockitoExtension.class)
class TxUtilsTest {

    @Mock
    private PlatformTransactionManager txManager;

    @Mock
    private TransactionStatus status;

    // ------------------------------------------------------------------
    // getTransaction
    // ------------------------------------------------------------------

    @Test
    @DisplayName("getTransaction(txManager) 는 기본 정의로 트랜잭션을 시작한다")
    void getTransactionWithManagerOnly() {
        when(txManager.getTransaction(any())).thenReturn(status);

        TransactionStatus result = TxUtils.getTransaction(txManager);

        assertThat(result).isSameAs(status);
        verify(txManager).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    @DisplayName("getTransaction(txManager, name) 는 이름이 설정된 정의로 트랜잭션을 시작한다")
    void getTransactionWithName() {
        when(txManager.getTransaction(any())).thenReturn(status);

        TransactionStatus result = TxUtils.getTransaction(txManager, "myTx");

        assertThat(result).isSameAs(status);
        verify(txManager).getTransaction(any(TransactionDefinition.class));
    }

    @Test
    @DisplayName("getTransaction(definition) 에 txManager 가 null 이면 NPE")
    void getTransactionNullManager() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        assertThatThrownBy(() -> TxUtils.getTransaction(null, def))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transactionManager");
    }

    @Test
    @DisplayName("getTransaction(definition) 에 definition 이 null 이면 NPE")
    void getTransactionNullDefinition() {
        assertThatThrownBy(() -> TxUtils.getTransaction(txManager, (TransactionDefinition) null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("definition");
    }

    // ------------------------------------------------------------------
    // commit / rollback
    // ------------------------------------------------------------------

    @Test
    @DisplayName("commit 은 미완료 상태에서 txManager.commit 호출")
    void commitWhenNotCompleted() {
        when(status.isCompleted()).thenReturn(false);

        TxUtils.commit(txManager, status);

        verify(txManager).commit(status);
    }

    @Test
    @DisplayName("commit 은 이미 완료된 상태에서는 commit 을 호출하지 않는다")
    void commitWhenCompleted() {
        when(status.isCompleted()).thenReturn(true);

        TxUtils.commit(txManager, status);

        verify(txManager, never()).commit(any());
    }

    @Test
    @DisplayName("commit 에 txManager 가 null 이면 NPE")
    void commitNullManager() {
        assertThatThrownBy(() -> TxUtils.commit(null, status))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transactionManager");
    }

    @Test
    @DisplayName("commit 에 status 가 null 이면 NPE")
    void commitNullStatus() {
        assertThatThrownBy(() -> TxUtils.commit(txManager, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("status");
    }

    @Test
    @DisplayName("rollback 은 미완료 상태에서 txManager.rollback 호출")
    void rollbackWhenNotCompleted() {
        when(status.isCompleted()).thenReturn(false);

        TxUtils.rollback(txManager, status);

        verify(txManager).rollback(status);
    }

    @Test
    @DisplayName("rollback 은 이미 완료된 상태에서는 rollback 을 호출하지 않는다")
    void rollbackWhenCompleted() {
        when(status.isCompleted()).thenReturn(true);

        TxUtils.rollback(txManager, status);

        verify(txManager, never()).rollback(any());
    }

    @Test
    @DisplayName("rollback 에 txManager 가 null 이면 NPE")
    void rollbackNullManager() {
        assertThatThrownBy(() -> TxUtils.rollback(null, status))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("transactionManager");
    }

    @Test
    @DisplayName("rollback 에 status 가 null 이면 NPE")
    void rollbackNullStatus() {
        assertThatThrownBy(() -> TxUtils.rollback(txManager, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("status");
    }

    // ------------------------------------------------------------------
    // execute (Consumer)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("execute(Consumer) 정상 흐름은 작업 수행 후 commit")
    void executeConsumerSuccess() {
        when(txManager.getTransaction(any())).thenReturn(status);
        when(status.isCompleted()).thenReturn(false);
        AtomicBoolean executed = new AtomicBoolean(false);

        TxUtils.execute(txManager, (Consumer<TransactionStatus>) s -> executed.set(true));

        assertThat(executed).isTrue();
        verify(txManager).commit(status);
        verify(txManager, never()).rollback(any());
    }

    @Test
    @DisplayName("execute(name, Consumer) 정상 흐름은 작업 수행 후 commit")
    void executeNamedConsumerSuccess() {
        when(txManager.getTransaction(any())).thenReturn(status);
        when(status.isCompleted()).thenReturn(false);
        AtomicBoolean executed = new AtomicBoolean(false);

        TxUtils.execute(txManager, "named", s -> executed.set(true));

        assertThat(executed).isTrue();
        verify(txManager).commit(status);
    }

    // ------------------------------------------------------------------
    // executeWithResult
    // ------------------------------------------------------------------

    @Test
    @DisplayName("executeWithResult 정상 흐름은 결과 반환 및 commit")
    void executeWithResultSuccess() {
        when(txManager.getTransaction(any())).thenReturn(status);
        when(status.isCompleted()).thenReturn(false);

        String result = TxUtils.executeWithResult(txManager, s -> "ok");

        assertThat(result).isEqualTo("ok");
        verify(txManager).commit(status);
    }

    @Test
    @DisplayName("executeWithResult(name) 정상 흐름은 결과 반환 및 commit")
    void executeWithResultNamedSuccess() {
        when(txManager.getTransaction(any())).thenReturn(status);
        when(status.isCompleted()).thenReturn(false);

        String result = TxUtils.executeWithResult(txManager, "named", s -> "ok2");

        assertThat(result).isEqualTo("ok2");
        verify(txManager).commit(status);
    }

    // ------------------------------------------------------------------
    // execute (Function) - 예외 처리 분기
    // ------------------------------------------------------------------

    @Test
    @DisplayName("execute(Function) 작업에서 RuntimeException 발생 시 rollback 후 재전파")
    void executeRuntimeExceptionTriggersRollback() {
        when(txManager.getTransaction(any())).thenReturn(status);
        when(status.isCompleted()).thenReturn(false);
        RuntimeException boom = new IllegalArgumentException("boom");

        assertThatThrownBy(() -> TxUtils.execute(txManager, new DefaultTransactionDefinition(),
                s -> {
                    throw boom;
                }))
                .isSameAs(boom);

        verify(txManager).rollback(status);
        verify(txManager, never()).commit(any());
    }

    @Test
    @DisplayName("execute(Function) 작업에서 Error 발생 시 rollback 후 재전파")
    void executeErrorTriggersRollback() {
        when(txManager.getTransaction(any())).thenReturn(status);
        when(status.isCompleted()).thenReturn(false);
        Error err = new StackOverflowError();

        assertThatThrownBy(() -> TxUtils.execute(txManager, new DefaultTransactionDefinition(),
                s -> {
                    throw err;
                }))
                .isSameAs(err);

        verify(txManager).rollback(status);
    }

    @Test
    @DisplayName("execute(Function) 작업에서 검사 예외 발생 시 rollback 후 TxWorkException 으로 래핑")
    void executeCheckedExceptionWrapped() throws Exception {
        when(txManager.getTransaction(any())).thenReturn(status);
        when(status.isCompleted()).thenReturn(false);

        @SuppressWarnings("unchecked")
        Function<TransactionStatus, String> work = org.mockito.Mockito.mock(Function.class);
        // Function.apply 는 검사 예외를 선언하지 않으므로 Answer 로 우회하여 던진다.
        when(work.apply(any())).thenAnswer(invocation -> {
            throw new Exception("checked failure");
        });

        assertThatThrownBy(() -> TxUtils.execute(txManager, new DefaultTransactionDefinition(), work))
                .isInstanceOf(TxUtils.TxWorkException.class)
                .hasFieldOrPropertyWithValue("errorCode", FrameworkErrorCode.INTERNAL_ERROR.getCode())
                .hasMessageContaining("transaction work fail");

        verify(txManager).rollback(status);
    }

    @Test
    @DisplayName("execute(Function) 에 work 가 null 이면 NPE")
    void executeNullWork() {
        assertThatThrownBy(() -> TxUtils.execute(txManager, new DefaultTransactionDefinition(), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("work");
    }

    // ------------------------------------------------------------------
    // defaultDefinition / newDefinition
    // ------------------------------------------------------------------

    @Test
    @DisplayName("defaultDefinition(null) 은 이름이 없는 정의를 반환")
    void defaultDefinitionNullName() {
        DefaultTransactionDefinition def = TxUtils.defaultDefinition(null);

        assertThat(def.getName()).isNull();
    }

    @Test
    @DisplayName("defaultDefinition(blank) 은 이름을 설정하지 않는다")
    void defaultDefinitionBlankName() {
        DefaultTransactionDefinition def = TxUtils.defaultDefinition("   ");

        assertThat(def.getName()).isNull();
    }

    @Test
    @DisplayName("defaultDefinition(name) 은 이름이 설정된 정의를 반환")
    void defaultDefinitionWithName() {
        DefaultTransactionDefinition def = TxUtils.defaultDefinition("txName");

        assertThat(def.getName()).isEqualTo("txName");
    }

    @Test
    @DisplayName("newDefinition 은 커스터마이저를 적용한다")
    void newDefinitionAppliesCustomizer() {
        DefaultTransactionDefinition def = TxUtils.newDefinition(
                d -> d.setName("customized"));

        assertThat(def.getName()).isEqualTo("customized");
    }

    @Test
    @DisplayName("newDefinition(null) 은 기본 정의를 반환")
    void newDefinitionNullCustomizer() {
        DefaultTransactionDefinition def = TxUtils.newDefinition(null);

        assertThat(def).isNotNull();
        assertThat(def.getName()).isNull();
    }

    @Test
    @DisplayName("getTransaction(txManager, name) 정상 위임 호출 횟수 검증")
    void getTransactionDelegationCount() {
        when(txManager.getTransaction(any())).thenReturn(status);

        TxUtils.getTransaction(txManager, "x");

        verify(txManager, times(1)).getTransaction(any(TransactionDefinition.class));
    }
}
