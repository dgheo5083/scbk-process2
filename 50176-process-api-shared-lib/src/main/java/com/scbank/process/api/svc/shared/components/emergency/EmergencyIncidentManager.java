package com.scbank.process.api.svc.shared.components.emergency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.core.component.ComponentOperation;
import com.scbank.process.api.fw.core.component.SharedComponent;
import com.scbank.process.api.svc.shared.components.emergency.dao.Ma3EmgncyDisablDao;
import com.scbank.process.api.svc.shared.components.emergency.dao.dto.Ma3EmgncyDisablResult;
import com.scbank.process.api.svc.shared.components.emergency.dto.EmergencyIncidentInfo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 제일은행 긴급장애정보 매니저 컴포넌트
 */
@Slf4j
@RequiredArgsConstructor
@Lazy
@SharedComponent(name = "제일은행 긴급장애정보 매니저 컴포넌트", author = "sungdon.choi")
public class EmergencyIncidentManager {

	/**
	 * 긴급장애정보
	 */
	private Map<String, EmergencyIncidentInfo> emgncyMap = new HashMap<>();
	
	/**
	 * 긴급장애공지 DAO 컴포넌트
	 */
	private final Ma3EmgncyDisablDao emgncyDisablDao;
	
	/**
	 * 초기화 처리
	 */
	@PostConstruct
	public void init() {
		this.load();
	}
	
	/**
	 * 긴급장애공지 목록 로드
	 */
	private void load() {
		try {
			List<Ma3EmgncyDisablResult> list = emgncyDisablDao.selectMa3EmgncyDisablList();
			if (CollectionUtils.isEmpty(list)) {
				return;
			}
			
			Map<String, EmergencyIncidentInfo> map = list.stream()
				.map((v) -> {
					return EmergencyIncidentInfo.builder()
							.id(v.getDisablId())
							.type(v.getDisablType())
							.cd(v.getDisablCd())
							.errCd(v.getDisablErrCd())
							.startTm(v.getDisablStartTm())
							.endTm(v.getDisablEndTm())
							.desc(v.getDisablDesc())
							.build();
				})
				.collect(Collectors.toMap(
						EmergencyIncidentInfo::getId, 
						Function.identity(), 
						(old, value) -> value));
			
			if (log.isDebugEnabled()) {
				log.debug("# 제일은행 긴급장애공지 목록 조회 성공, 개수 {}", map.size());
			}
			
			this.emgncyMap.putAll(map);
		} catch (Exception e) {
			log.error("긴급장애공지 조회 중 오류 발생 [" + e.getMessage() + "]", e);
		}
	}
	
	/**
	 * 
	 * @param type
	 * @param code
	 * @return
	 */
	@ComponentOperation(name = "긴급장애공지 정보 획득", author = "sungdon.choi")
	public List<EmergencyIncidentInfo> get(String type, String code) {
		if (CollectionUtils.isEmpty(emgncyMap)) {
			return List.of();
		}
		
		return emgncyMap.values()
				.stream()
				.filter(v -> type.equals(v.getType()) && code.equals(v.getCd()))
				.collect(Collectors.toList());
	}
}
