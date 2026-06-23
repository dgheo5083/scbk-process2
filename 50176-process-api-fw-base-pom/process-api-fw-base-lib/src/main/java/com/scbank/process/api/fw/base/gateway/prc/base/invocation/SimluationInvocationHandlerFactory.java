package com.scbank.process.api.fw.base.gateway.prc.base.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

import com.scbank.process.api.fw.base.gateway.prc.PRCGatewayProperties.Gateway;
import com.scbank.process.api.fw.base.gateway.prc.PRCGatewayProperties.SimulationConfig;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationGatewayResolver;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationGatewayResolver.ResolvedGateway;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationKeyResolver;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.SimulationResponseFactory;
import com.scbank.process.api.fw.base.gateway.prc.base.simulation.annotation.SimulationMode;
import com.scbank.process.api.fw.core.log.trace.TraceSection;

import feign.Target;
import lombok.RequiredArgsConstructor;

/**
 * 로컬 시뮬레이션 응답 처리 용 InvocationHandlerFactory 구현 클래스
 */
@RequiredArgsConstructor
public class SimluationInvocationHandlerFactory extends BaseInvocationHandlerFactory {

    private final SimulationGatewayResolver gatewayResolver;
    
    private final SimulationResponseFactory responseFactory;
    
    @SuppressWarnings("rawtypes")
    @Override
    public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
        InvocationHandler baseHandler = defaultFactory.create(target, dispatch);
        return (proxy, method, args) -> {
        	String url = target.url() + path(method);

            try {
            	Optional<ResolvedGateway> resolved = this.gatewayResolver.resolve(target.url());
            	if (resolved.isEmpty()) {
            		this.beginTrace(TraceSection.PRC_CALL, url);
            		return baseHandler.invoke(proxy, method, args); 
            	}
         
            	// 시뮬레이션모드 여부 확인
            	SimulationMode simulation = method.getAnnotation(SimulationMode.class);
            	if (simulation == null) {
            		this.beginTrace(TraceSection.PRC_CALL, url);
            		return baseHandler.invoke(proxy, method, args);
            	}
            	
            	//어플리케이션 별 설정정보 획득
            	Gateway gateway = resolved.get().config();
            	//시뮬레이션 모드 활성화여부 획득
            	boolean enabled = this.enabledSimulation(gateway);
            	//시뮬레이션 대응답 파일 경로 획득
            	String configLocation = this.simulationConfigLocation(gateway);
            	
            	if (!enabled) {
            		this.beginTrace(TraceSection.PRC_CALL, url);
            		return baseHandler.invoke(proxy, method, args);
            	}
            	
            	this.beginTrace(TraceSection.PRC_SIMULATION, url);
            	
            	Object simulationResponse = this.responseFactory.createOrThrow(target, method, simulation, configLocation);
            	if (simulationResponse != null) {
            		return simulationResponse;
            	}
            	throw new IllegalStateException("Simulation json not found for " + SimulationKeyResolver.fileName(method));
            	
            } catch (Exception ex) {
                this.failTrace(ex);
                throw ex;
            } finally {
            	this.endTrace();
            }
        };
    }
    
    /**
     * 시뮬레이션 활성화여부를 가져온다.
     * @param gateway {@link Gateway}
     * @return
     */
    private boolean enabledSimulation(Gateway gateway) {
    	if (gateway == null) {
    		return false;
    	}
    	
    	SimulationConfig simulation = gateway.getSimulation();
    	if (simulation == null) {
    		return false;
    	}
    	
    	return simulation.isEnabled();
    }
    
    /**
     * 타켓별 시뮬레이션 응답 json 경로를 가져온다.
     * @param gateway {@link Gateway}
     * @return
     */
    private String simulationConfigLocation(Gateway gateway) {
    	if (gateway == null) {
    		return null;
    	}
    	
    	SimulationConfig simulation = gateway.getSimulation();
    	if (simulation == null) {
    		return null;
    	}
    	
    	return simulation.getConfigLocation();
    }
}
