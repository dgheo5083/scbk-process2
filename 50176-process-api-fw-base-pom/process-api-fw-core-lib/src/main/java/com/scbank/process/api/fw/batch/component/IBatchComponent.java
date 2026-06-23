package com.scbank.process.api.fw.batch.component;

import com.scbank.process.api.fw.batch.context.IBatchContext;

/**
 * 프레임워크 배치 컴포넌트 인터페이스
 */
public interface IBatchComponent {

	/**
	 * 배치 업무 수행
	 * 
	 * @param ctx 프레임워크 배치 실행 컨텍스트
	 */
	void execute(IBatchContext ctx);
}
