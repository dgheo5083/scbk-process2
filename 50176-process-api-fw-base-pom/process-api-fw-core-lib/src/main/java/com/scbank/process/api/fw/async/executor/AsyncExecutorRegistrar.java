package com.scbank.process.api.fw.async.executor;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.async.AsyncProperties;
import com.scbank.process.api.fw.async.AsyncProperties.ThreadPoolProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
public class AsyncExecutorRegistrar implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, BeanFactoryAware, PriorityOrdered {

	private Environment environment;
	
	private BeanFactory beanFactory;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
		Binder binder = Binder.get(environment);
		AsyncProperties properties = binder.bind("csl.async", AsyncProperties.class).get();
		
		if (log.isInfoEnabled()) {
            log.info("# 프레임워크 Async Thread Pool Bean 등록 시작");
        }
		
		if (CollectionUtils.isEmpty(properties.getExecutors())) {
			return;
		}
		
		for (Map.Entry<String, ThreadPoolProperties> e : properties.getExecutors().entrySet()) {
			String name = e.getKey();
			String beanName = name.endsWith("Executor") ? name : name + "Executor";
			
			if (registry.containsBeanDefinition(beanName)) {
				continue;
			}
			
			RootBeanDefinition bd = new RootBeanDefinition(ThreadPoolTaskExecutor.class);
			bd.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			
			bd.setInstanceSupplier(() -> createExecutor(beanName, e.getValue()));
			
			registry.registerBeanDefinition(beanName, bd);
		}
	}
	
	
	
	/**
	 * ThreadPoolTaskExecutor 생성
	 * @param beanName 빈명
	 * @param properties {@link ThreadPoolProperties}
	 * @return
	 */
	private ThreadPoolTaskExecutor createExecutor(String beanName, ThreadPoolProperties properties) {
		TaskDecorator taskDecorator = this.beanFactory.getBean(TaskDecorator.class);
		return AsyncExecutorFactory.create(properties, taskDecorator);
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
