package com.scbank.process.api.fw.core.component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DAO 계층 클래스에 부여하는 컴포넌트 어노테이션입니다.
 * <p>
 * 데이터베이스 연동 기능을 담당하는 DAO 클래스에 부여하며, 해당 DAO가 접근하는 데이터베이스, 기능 식별자 및 설명을 메타정보로
 * 정의합니다.
 * <p>
 * 이 메타정보는 트레이스 로깅, DAO 문서화, 관리 UI, 동적 DAO 등록 등에서 활용됩니다.
 * </p>
 *
 * <pre>{@code
 * &#64;DaoComponent(
 *     database = "oracle",
 *     id = "USER_DAO",
 *     name = "사용자 DAO",
 *     description = "사용자 테이블에 대한 CRUD 처리",
 *     author = "홍길동"
 * )
 * public class UserDao {
 *     ...
 * }
 * }</pre>
 *
 * @author gasigol
 * @version 1.0
 * @since 25. 4. 17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DaoComponent {

    /**
     * 이 DAO가 연결되는 데이터베이스 이름 (예: oracle, mysql, maria)
     */
    String database();

    /**
     * DAO의 고유 식별자 (예: USER_DAO, ACCOUNT_DAO)
     */
    String id() default "";

    /**
     * DAO 이름 또는 설명용 간략한 라벨
     */
    String name() default "";

    /**
     * 상세 설명 (DAO의 주요 기능 또는 목적)
     */
    String description() default "";

    /**
     * DAO 작성자 또는 담당자 이름
     */
    String author() default "";

    /**
     * DAO 버전 (기본값: 1.0)
     */
    String version() default "1.0";

    /**
     * 사용여부 (기본값: true)
     */
    boolean enabled() default true;
}
