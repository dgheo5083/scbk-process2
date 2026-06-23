package com.scbank.process.api.svc.shared.channel.interceptors;

import org.springframework.web.servlet.HandlerInterceptor;

import com.scbank.process.api.fw.channel.context.IServiceContext;
import com.scbank.process.api.fw.channel.context.ServiceContextHolder;
import com.scbank.process.api.fw.channel.exception.ServiceTimeException;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo;
import com.scbank.process.api.fw.channel.service.metadata.ServiceDefinitionMetadata.ServiceTimeInfo.TimeRange;
import com.scbank.process.api.fw.common.holiday.IHolidayManager;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.utils.DateUtils;
import com.scbank.process.api.fw.core.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 요청 서비스 시간 체크 인터셉터 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class ServiceEndpointTimeCheckInterceptor implements HandlerInterceptor {

    /**
     * 프레임워크 휴일관리 매니저
     */
    private final IHolidayManager holidayManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        IServiceContext ctx = ServiceContextHolder.getContext();

        // 서비스 정의 메타데이터 획득
        ServiceDefinitionMetadata definition = ctx.serviceDefinition();
        if (definition == null) {
        	return true;
        }

        if (log.isDebugEnabled()) {
            log.debug("# URI:[{}] 서비스 이용시간 체크 시작", definition.getUrl());
        }

        // 서비스이용시간정보 획득
        ServiceTimeInfo serviceTime = definition.getServiceTime();
        if (serviceTime == null) {
            if (log.isDebugEnabled()) {
                log.debug("# URI:[{}] 서비스이용시간정보 없음, 이용시간체크 skip 처리", definition.getUrl());
            }
            return true;
        }

        // 서비스 이용시간 활성화 여부 체크
        if (!serviceTime.enabled()) {
            if (log.isDebugEnabled()) {
                log.debug("# URI:[{}] 서비스이용시간정보 비활성화 처리, 이용시간체크 skip 처리", definition.getUrl());
            }
            return true;
        }

        // 서비스 영업일/휴일 이용시간 정보 획득
        TimeRange timeRange = this.getTimeRange(serviceTime);
        if (timeRange == null) {
            if (log.isDebugEnabled()) {
                log.debug("# URI:[{}] 서비스 영업일/휴일 이용시간 설정없음, 이용시간체크 skip 처리", definition.getUrl());
            }
            return true;
        } else {
        	// 서비스 이용시간 체크
            String startTime = timeRange.startTime();
            String endTime = timeRange.endTime();

            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                if (log.isDebugEnabled()) {
                    log.debug("# URI:[{}] 미설정 이용시간 존재, 이용시간체크 skip 처리, startTime: {}, endTime: {}",
                            definition.getUrl(), startTime, endTime);
                }
                return true;
            }

            boolean isTimeValid = DateUtils.isNowBetweenHHmm(startTime, endTime);
            if (!isTimeValid) {
                throw new ServiceTimeException(
                        FrameworkErrorCode.BIZTIME_OUT_OF_BUSINESS_HOURS.getCode(),
                        FrameworkErrorCode.BIZTIME_OUT_OF_BUSINESS_HOURS.getMessage());
            }
        }
        return true;
    }

    /**
     * 
     * @param serviceTime
     * @return
     */
    private TimeRange getTimeRange(ServiceTimeInfo serviceTime) {
        String today = DateUtils.getCurrentDate();
        boolean isHoliday = DateUtils.isHoliday(today) || holidayManager.isHoliday(today);
        return isHoliday ? serviceTime.holiday() : serviceTime.businessDay();
    }
}
