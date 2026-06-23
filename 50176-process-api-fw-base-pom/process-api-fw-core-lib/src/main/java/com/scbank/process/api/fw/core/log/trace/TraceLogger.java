package com.scbank.process.api.fw.core.log.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 트레이스 노드를 트리 구조 형태로 로깅하는 유틸리티 클래스입니다.
 * 각 노드는 실행 시간, 상태(성공/실패), 섹션(CONTROLLER/SERVICE 등)을 포함하여 출력됩니다.
 *
 * 예시 출력:
 * Trace Start
 * └── [✓] CONTROLLER - /api/login (120 ms)
 *     ├── [✓] SERVICE - LoginService.authenticate() (45 ms)
 *     └── [✗] DAO - UserRepository.findUser() (10 ms) - 사용자 없음
 * Trace End
 * </pre>
 *
 * @author sungdon.choi
 * @since 25. 4. 17.
 */
public class TraceLogger {

    private static final Logger logger = LoggerFactory.getLogger("trace.logging");

    public static void logTraceTree(TraceNode root) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTrace Start [URI: ").append(root.getLabel()).append("]\n");
        printNode(sb, root, "", true);
        sb.append("Trace End");
        logger.info(sb.toString());
    }

    private static void printNode(StringBuilder sb, TraceNode node, String indent, boolean isLast) {
        String statusIcon = node.success ? "O" : "X";
        String statusText = node.success ? "성공" : "실패";
        String duration = String.format("%d", node.getDurationMillis());

        sb.append(indent)
                .append(isLast ? " └── " : " ├── ")
                .append(String.format("[%s] %s - %s [%s|%sms]",
                        statusIcon, node.getSection(), node.getLabel(), statusText, duration))
                .append("\n");

        // if (!node.success && node.errorMessage != null) {
        // sb.append(indent)
        // .append(isLast ? " " : "│ ")
        // .append("↳ cause: ").append(node.errorMessage).append("\n");
        // }

        for (int i = 0; i < node.getChildren().size(); i++) {
            printNode(sb, node.getChildren().get(i),
                    indent + (isLast ? "    " : "│   "),
                    i == node.getChildren().size() - 1);
        }
    }
}
