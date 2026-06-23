package com.scbank.process.api.fw.message.context;

/**
 * 메시지 변환(직렬화/역직렬화) 시 Context를 ThreadLocal에 보관하는 헬퍼 클래스
 *
 * 주로 메시지 변환 처리 내부에서 변환 시,
 * 현재 변환 중인 Context(MessageContext)를 전역 ThreadLocal로 관리합니다.
 * 
 * 사용 예:
 * - 변환 옵션(SerializationOptions, DeserializationOptions) 접근
 * - 현재 경로(Path) 기반 필드 접근
 * - 변환 과정 중 Masking, Default Value 설정 등
 * 
 * 반드시 변환 시작 시점(set), 변환 종료 시점(clear) 관리를 명확히 해야 합니다.
 * 
 * @author sungdon.choi
 */
public class MessageContextHolder {

    /** ThreadLocal에 MessageContext를 보관 */
    private static final ThreadLocal<MessageContext> threadLocal = new ThreadLocal<>();

    /**
     * 현재 스레드에 저장된 MessageContext를 반환합니다.
     *
     * @return 현재 Context, 없으면 null
     */
    public static MessageContext get() {
        return threadLocal.get();
    }

    /**
     * 현재 스레드에 MessageContext를 설정합니다.
     *
     * @param ctx 설정할 MessageContext
     */
    public static void set(MessageContext ctx) {
        threadLocal.set(ctx);
    }

    /**
     * 현재 스레드의 MessageContext를 제거합니다.
     * (메모리 누수 방지를 위해 반드시 호출)
     */
    public static void clear() {
        threadLocal.remove();
    }
}
