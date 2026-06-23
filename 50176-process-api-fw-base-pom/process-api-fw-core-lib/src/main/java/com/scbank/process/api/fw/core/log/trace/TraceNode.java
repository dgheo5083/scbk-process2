package com.scbank.process.api.fw.core.log.trace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * <pre>
 * 트레이스 트리의 노드 객체. 각 실행 단위(메서드/요청 등)를 하나의 노드로 표현합니다.
 * 트리 구조로 연결되어 있으며, 시작 시간/종료 시간, 성공 여부, 섹션 등의 정보를 포함합니다.
 *
 * 예: 컨트롤러 → 서비스 → DAO → 외부 시스템 호출 등의 구조를 트리 형태로 기록.
 * </pre>
 *
 * @author sungdon.choi
 * @since 25. 4. 17.
 */
@Data
public class TraceNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 부모 노드 (null이면 루트 노드)
     */
    private final TraceNode parent;

    /**
     * 노드 라벨 (ex: 메서드명 또는 URI)
     */
    private final String label;

    /**
     * 실행 계층 (CONTROLLER, SERVICE, DAO 등)
     */
    private final TraceSection section;

    /**
     * 자식 노드 목록
     */
    private final List<TraceNode> children = new ArrayList<>();

    /**
     * 성공 여부
     */
    public boolean success = true;

    /**
     * 실패 시 에러 메시지
     */
    public String errorMessage;

    /**
     * 시작 시간 (nanoTime 기준)
     */
    public long startTime;

    /**
     * 종료 시간 (nanoTime 기준)
     */
    public long endTime;

    /**
     * 트레이스 노드 생성자
     *
     * @param parent  부모 노드
     * @param label   노드 라벨
     * @param section 실행 계층
     */
    public TraceNode(TraceNode parent, String label, TraceSection section) {
        this.parent = parent;
        this.label = label;
        this.section = section;
    }

    /**
     * 실행 시작 시 호출 (시작 시간 기록)
     */
    public void markStart() {
        this.startTime = System.nanoTime();
    }

    /**
     * 실행 종료 시 호출 (종료 시간 기록)
     */
    public void markEnd() {
        this.endTime = System.nanoTime();
    }

    /**
     * 실행 시간(ms) 계산
     */
    public long getDurationMillis() {
        return (endTime - startTime) / 1_000_000;
    }

    /**
     * 자식 노드 추가
     */
    public void addChild(TraceNode child) {
        children.add(child);
    }

    /**
     * 실패 처리 및 에러 메시지 저장
     */
    public void fail(String message) {
        this.success = false;
        this.errorMessage = message;
    }
}
