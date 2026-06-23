package com.scbank.process.api.fw.common.property.impl;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.common.CommonProperties.PropertyConfig;
import com.scbank.process.api.fw.common.property.IPropertyManager;
import com.scbank.process.api.fw.core.component.CommonComponent;
import com.scbank.process.api.fw.core.concurrent.lock.ReadWriteLockSupport;
import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@CommonComponent(name = "프레임워크/공통 프로퍼티 매니저 컴포넌트", author = "sungdon.choi")
public class DefaultPropertyManager extends ReadWriteLockSupport implements IPropertyManager {

	private final PropertyConfig config;

	private final CompositeConfiguration properties = new CompositeConfiguration();

	@Override
	public void init() {
		this.withWrite(() -> {
			try {
				List<String> configLocations = config.configLocations();
				if (CollectionUtils.isEmpty(configLocations)) {
					return;
				}

				for (String configLocation : configLocations) {
					if (log.isInfoEnabled()) {
						log.info("# 프레임워크 공통프로퍼티 목록 로드, 경로: [{}]", configLocation);
					}

					if (StringUtils.isEmpty(configLocation)) {
						if (log.isInfoEnabled()) {
							log.info("# 프레임워크 공통프로퍼티 목록 로드 실패, 설정 파일 경로 미설정");
						}
						return;
					}

					PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
					Resource[] resources = resolver.getResources(configLocation);
					if (resources.length == 0) {
						if (log.isInfoEnabled()) {
							log.info("# 프레임워크 공통프로퍼티 파일이 없습니다. 경로: [{}]", configLocation);
						}
						return;
					}

					CompositeConfiguration composite = new CompositeConfiguration();

					for (Resource resource : resources) {
						if (!resource.exists()) {
							continue;
						}

						URL url = resource.getURL();
						Parameters params = new Parameters();
						
						FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(
								PropertiesConfiguration.class)
								.configure(params.properties().setURL(url).setEncoding(StandardCharsets.UTF_8.name()));
						PropertiesConfiguration config = builder.getConfiguration();
						
						composite.addConfiguration(config);

						if (log.isInfoEnabled()) {
							log.info("# 프레임워크 공통프로퍼티 파일 로드, 경로: {}", resource.getFilename());
						}
					}

					properties.addConfiguration(composite);

					if (log.isInfoEnabled()) {
						log.info("# 프레임워크 공통프로퍼티 초기화 완료");
					}
				}
			} catch (Exception e) {
				throw new FrameworkRuntimeException(FrameworkErrorCode.INIT_CONFIG_LOAD_FAILED,
						"PropertyManager initialization failed", e);
			}
		});
	}

	@Override
	public void reload() {
		// Apache Commons는 기본적으로 reload 지원 없음
		// 구현 시 polling reload strategy 또는 다시 init()
		init();
	}

	@Override
	public String getString(String key, String defaultValue) {
		return withRead(() -> properties == null ? defaultValue : properties.getString(key, defaultValue));
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return withReadInt(() -> properties == null ? defaultValue : properties.getInteger(key, defaultValue));
	}

	@Override
	public boolean getBoolean(String key) {
		return withRead(() -> properties.getBoolean(key, false));
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return withRead(() -> properties == null ? defaultValue : properties.getBoolean(key, defaultValue));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key, T defaultValue) {
		return withRead(() -> {
			Object value = properties.getProperty(key);
			return value != null ? (T) value : defaultValue;
		});
	}
}
