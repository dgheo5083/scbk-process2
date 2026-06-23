package com.scbank.process.api.fw.batch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * <pre>
 * packageName    : co.kr.framework.batch
 * fileName       : BatchProperties
 * author         : gasigol
 * date           : 25. 4. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 4. 13.        gasigol       최초 생성
 * </pre>
 */
@Data
@ConfigurationProperties(prefix = "batch")
public class BatchProperties {

    /**
     * 배치 활성화 여부
     */
    private boolean enabled;

    /**
     * 배치 메타데이터 XML 경로
     */
    private String configLocation;

    /**
     *
     */
    private boolean autoStartup = false;
}
