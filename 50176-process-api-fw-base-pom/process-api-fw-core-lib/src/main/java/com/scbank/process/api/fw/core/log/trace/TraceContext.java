package com.scbank.process.api.fw.core.log.trace;

import java.io.Serializable;
import lombok.Data;

/**
 * <pre>
 * 트레이스 컨텍스트 클래스.
 * 각 요청에 대해 TraceNode 트리 구조를 구성하며,
 * 실행 흐름의 시작/종료/에러 마킹 기능을 제공합니다.
 * 루트 노드는 보통 URI 단위로 생성되며, 이후 begin()을 통해 자식 노드를 구성합니다.
 *
 * TraceLogger.logTraceTree()를 통해 트리 전체를 출력할 수 있습니다.
 * </pre>
 *
 * @author sungdon.choi
 * @since 25. 4. 17.
 */
@Data
public class TraceContext implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 루트 노드: 요청 전체 흐름의 시작점 (예: URI 단위)
     */
    private final TraceNode root;

    /**
     * 현재 실행 중인 노드 (자식 begin 시 이동함)
     */
    private TraceNode current;

    /**
     * 컨텍스트 생성자. 루트 노드를 구성하며 바로 markStart() 호출
     *
     * @param rootLabel 루트 노드 라벨 (예: URI)
     */
    public TraceContext(String rootLabel) {
        this(rootLabel, TraceSection.CTRL);
    }

    /**
     * 컨텍스트 생성자. 루트 노드를 구성하며 바로 markStart() 호출
     * 
     * @param rootLabel 루트 노드 라벨 (예: URI, 배치 컴포넌트ID)
     * @param section   트레이스 섹션 {@link TraceSection}
     */
    public TraceContext(String rootLabel, TraceSection section) {
        this.root = new TraceNode(null, rootLabel, section);
        this.current = root;
        this.root.markStart();
    }

    /**
     * 새로운 트레이스 노드를 현재 노드의 자식으로 시작합니다.
     *
     * @param label   실행 단위 이름 (예: 클래스.메서드명)
     * @param section 실행 계층 (CONTROLLER, SERVICE, DAO 등)
     */
    public void begin(String label, TraceSection section) {
    	if (current == null) {
    		return;
    	}
    	
        TraceNode child = new TraceNode(current, label, section);
        
        current.addChild(child);
        current = child;
        
        child.markStart();
    }

    /**
     * 현재 실행 중인 노드를 종료 처리하고, 부모 노드로 이동합니다.
     */
    public void end() {
    	if (current == null) {
    		return;
    	}
        current.markEnd();
        current = current.getParent();
    }

    /**
     * 현재 또는 루트 노드를 실패로 마킹합니다. 예외 메시지를 포함하여 기록합니다.
     *
     * @param ex 발생한 예외
     */
    public void fail(Throwable ex) {
        TraceNode node = getCurrent();
        if (node != null) {
            node.fail(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } else if (root != null) {
            root.fail(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    /**
     * 현재 노드를 반환합니다.
     *
     * @return 현재 실행 중인 TraceNode
     */
    public TraceNode getCurrent() {
        return current;
    }
}
