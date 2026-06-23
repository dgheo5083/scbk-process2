package com.scbank.process.api.fw.core.log.trace.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.scbank.process.api.fw.core.component.BatchComponent;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.DaoComponent;
import com.scbank.process.api.fw.core.component.ServiceComponent;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.fw.core.log.trace.TraceContext;
import com.scbank.process.api.fw.core.log.trace.TraceContextHolder;
import com.scbank.process.api.fw.core.log.trace.TraceSection;

/**
 * 
 * <p>
 * 
 * Aspect 기반 트레이싱 AOP 클래스입니다.
 * </p>
 * <p>
 * {@link ComponentOperation} 어노테이션이 선언된 메서드에 대해 실행 전후로
 * {@link TraceContext}에
 * 트레이스 노드를 추가하고, 실행 시간과 실패 여부를 기록합니다.
 * </p>
 *
 * <ul>
 * <li>라벨(label)은 기본적으로 클래스.메서드() 형식이며,
 * {@link ComponentOperation#name()}이 존재하면
 * 이를 우선 사용 합니다.</li>
 * <li>섹션(section)은 클래스 어노테이션(Service, DaoComponent 등)을 기준으로 자동
 * 분류됩니다.</li>
 * <li>실행 중 예외 발생 시 자동으로 실패로 마킹되며, 에러 메시지가 로그 트리에 함께 기록됩니다.</li>
 * </ul>
 *
 * <p>
 * </p>
 * 
 * @author sungdon.choi
 * @since 25. 4. 23.
 */
@Aspect
@Component
public class TraceLogAop {

    /**
     * <pre>
     * ComponentOperation 어노테이션이 붙은 메서드의 실행을 감싸서 트레이스 로그를 기록 합니다.
     * 메서드 실행 전 TraceContext 에 begin()으로 시작 시점을 기록 하고,
     * 메서드 실행 후 end()로 종료 시점을 기록 합니다.
     * 예외 발생 시 fail()로 상태를 기록 합니다.
     * </pre>
     *
     * @param joinPoint AOP JoinPoint (실행 대상 메서드 정보 포함)
     * @return 실제 메서드 실행 결과
     * @throws Throwable 실행 중 발생한 예외
     */
    @Around("@annotation(com.scbank.process.api.fw.core.component.ComponentOperation)")
    public Object traceAround(ProceedingJoinPoint joinPoint) throws Throwable {	
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (signature == null) {
        	return joinPoint.proceed();
        }
        
        Method method = signature.getMethod();
        Class<?> clazz = method.getDeclaringClass();

        ComponentOperation op = method.getAnnotation(ComponentOperation.class);
      //트레이스여부 false인경우 처리
    	if (op != null && !op.trace()) {
    		return joinPoint.proceed();
    	}

        String label = clazz.getSimpleName() + "." + method.getName();
        TraceSection section = resolveSectionFromClass(clazz);

        TraceContext context = TraceContextHolder.get().orElse(null);
        if (context != null) {
            context.begin(label, section);
        }

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            if (context != null) {
                context.getCurrent().fail(ex.getMessage());
            }
            throw ex;
        } finally {
            if (context != null) {
                context.end();
            }
        }
    }

    /**
     * <pre>
     * 클래스에 붙은 어노테이션을 기준으로 TraceSection 판별 합니다.
     * ex: @ServiceComponent, @Service → SERVICE, @Component, @SharedComponent → SHARED
     * </pre>
     *
     * @param clazz 대상 클래스
     * @return 해당 클래스의 TraceSection 구분값
     */
    private TraceSection resolveSectionFromClass(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(RestController.class)) {
            return TraceSection.CTRL;
        } else if (clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(ServiceComponent.class)) {
            return TraceSection.SVC;
        } else if (clazz.isAnnotationPresent(Repository.class) || clazz.isAnnotationPresent(DaoComponent.class)) {
            return TraceSection.DAO;
        } else if (clazz.isAnnotationPresent(SharedComponent.class)) {
            return TraceSection.SHARED;
        } else if (clazz.isAnnotationPresent(CommonComponent.class) || clazz.isAnnotationPresent(Component.class)) {
            return TraceSection.COM;
        } else if (clazz.isAnnotationPresent(BatchComponent.class)) {
            return TraceSection.BATCH;
        } else {
            return TraceSection.NONE;
        }
    }
}
