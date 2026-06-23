package com.scbank.process.api.fw.core.lifecycle;

import java.util.List;

/**
 * 시스템 전반의 Reloadable 컴포넌트를 일괄 제어하는 매니저 인터페이스
 */
public interface IReloadManager {

    List<IReloadable> getReloadables();

    void reload(); // 전체 리로드

    void reload(Class<? extends IReloadable> type); // 특정 타입 리로드

    void reload(String simpleClassName); // 클래스 이름 기준 리로드 (선택적)
}
