package com.scbank.process.api.fw.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperProxy;
import org.mybatis.spring.mapper.MapperFactoryBean;

import com.scbank.process.api.fw.dao.paging.PagingRequestContext;

import lombok.RequiredArgsConstructor;

/**
 * Dao 컴포넌트 팩토리 빈 클래스
 * 
 * @author sungdon.choi
 */
public class DaoComponentFactoryBean<T> extends MapperFactoryBean<T> {

	/**
	 * 생성자
	 * 
	 * @param mapperInterface mapperInterface 클래스 타입
	 */
	public DaoComponentFactoryBean(Class<T> mapperInterface) {
		super(mapperInterface);
	}

	@Override
	public T getObject() throws Exception {
		T proxy = super.getObject();
		return wrapWithCustomProxy(proxy);
	}

	@SuppressWarnings("unchecked")
	private T wrapWithCustomProxy(T mapper) {
		InvocationHandler handler = Proxy.getInvocationHandler(mapper);
		if (handler instanceof MapperProxy) {
			return (T) Proxy.newProxyInstance(
					mapper.getClass().getClassLoader(),
					new Class[] { this.getMapperInterface() },
					new DaoComponentMethodInvocationHandler((MapperProxy<T>) handler));
		}
		return mapper;
	}

	@RequiredArgsConstructor
	private static class DaoComponentMethodInvocationHandler implements InvocationHandler {

		private final MapperProxy<?> delegate;

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Parameter[] parameters = method.getParameters();

			Integer offset = null;
			Integer limit = null;

			for (int i = 0; i < parameters.length; i++) {
				Param param = parameters[i].getAnnotation(Param.class);
				if (param != null) {
					if ("offset".equals(param.value()) && args[i] instanceof Integer) {
						offset = (Integer) args[i];
						args[i] = offset; // Optional: 수정하려면 여기서 바꿈
					} else if ("limit".equals(param.value()) && args[i] instanceof Integer) {
						limit = (Integer) args[i];
						args[i] = limit + 1;
					}
				}
			}

			// 페이징 컨텍스트에 저장 (선택)
			if (offset != null && limit != null) {
				PagingRequestContext.set(offset, limit); // 실제 limit로 기록
			}

			try {
				return delegate.invoke(proxy, method, args);
			} finally {
				PagingRequestContext.clear();
			}
		}
	}
}