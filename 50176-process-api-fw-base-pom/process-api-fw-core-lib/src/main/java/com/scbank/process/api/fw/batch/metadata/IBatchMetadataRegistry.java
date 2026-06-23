package com.scbank.process.api.fw.batch.metadata;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 프레임워크 배치메타데이터 레지스트리 인터페이스
 * 
 * @author sungdon.choi
 */
public interface IBatchMetadataRegistry extends InitializingBean, DisposableBean {

	@Override
	default void afterPropertiesSet() throws Exception {
		init();
	}

	void init();

	@Override
	default void destroy() throws Exception {

	}

	List<IBatchMetadata> getMetadatas();
}
