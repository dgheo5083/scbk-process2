package com.scbank.process.api.fw.batch.metadata.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.batch.BatchProperties;
import com.scbank.process.api.fw.batch.metadata.IBatchJobMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchMetadataRegistry;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata;
import com.scbank.process.api.fw.batch.metadata.IBatchTriggerMetadata.TriggerType;
import com.scbank.process.api.fw.core.utils.StringUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프레임워크 배치 메타데이터 정보 레지스트리 구현 클래스
 * 
 * @author sungdon.choi
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultBatchMetadataRegistry implements IBatchMetadataRegistry {

	/**
	 * 프레임워크 배치 관련 설정
	 */
	private final BatchProperties properties;

	/**
	 * 프레임워크 배치 메타데이터 목록
	 */
	@Getter
	private List<IBatchMetadata> metadatas = new ArrayList<>();

	@Override
	public void init() {
		if (log.isInfoEnabled()) {
			log.info("# 프레임워크 배치 메타데이터 로드 시작");
		}

		this.load();
	}

	/**
	 * 프레임워크 배치 메타데이터를 로드한다.
	 */
	private void load() {
		try {
			String configLocation = this.properties.getConfigLocation();
			PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

			Resource[] resources = resourcePatternResolver.getResources(configLocation);
			if (resources == null || resources.length == 0) {
				if (log.isInfoEnabled()) {
					log.info("# 프레임워크 배치 메타데이터 XML 파일이 존재하지 않음.");
				}
				return;
			}

			List<IBatchMetadata> metadatas = new ArrayList<>();

			for (Resource resource : resources) {
				try {
					metadatas.add(this.parseBatchMetadata(resource));
				} catch (Exception e) {
					log.error("# 프레임워크 배치 메타데이터 XML 파일 로드 중 오류 발생, 경로: {}, 오류내용: {} ", resource.getFilename(),
							e.getMessage(), e);
				}
			}

			this.metadatas.addAll(metadatas);

		} catch (Exception e) {
			log.error("# 프레임워크 배치 메타데이터 로드 중 오류 발생 " + e.getMessage(), e);
		}
	}

	/**
	 * 프레임워크 배치 메타데이터 XML 파일을 파상하여 배치메타데이터를 리턴한다.
	 * 
	 * @param resource 배치 메타데이터 XML 리소스
	 * @return XML 파일을 파상하여 얻은 배치메타데이터 정보
	 */
	private IBatchMetadata parseBatchMetadata(Resource resource) throws Exception {
		try (InputStream in = resource.getInputStream()) {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(in);

			Element batchDefinition = document.getRootElement();
			Element root = batchDefinition.getChild("batch");

			String id = StringUtils.defaultString(root.getAttributeValue("id")).trim();
			String description = StringUtils.defaultString(root.getAttributeValue("description")).trim();
			String targetNode = StringUtils.defaultString(root.getAttributeValue("targetNode")).trim();

			DefaultBatchMetadata batchMetadata = new DefaultBatchMetadata();
			batchMetadata.setId(id);
			batchMetadata.setDescription(description);
			batchMetadata.setTargetNode(targetNode);
			batchMetadata.setBatchJobMetadata(this.parseBatchJobMetadata(root.getChild("job")));
			batchMetadata.setBatchTriggerMetadata(this.parseBatchTriggerMetadata(root.getChild("trigger")));
			return batchMetadata;
		}
	}

	/**
	 * 배치 작업 메타데이터 정보 생성
	 * 
	 * @param element
	 * @return
	 */
	private IBatchJobMetadata parseBatchJobMetadata(Element element) {
		Element componentEl = element.getChild("component");
		String componentId = StringUtils.defaultString(componentEl.getAttributeValue("id")).trim();

		Map<String, String> parameters = new HashMap<>();
		Element parametersEl = element.getChild("parameters");
		if (parametersEl != null) {
			List<Element> children = parametersEl.getChildren();
			if (!CollectionUtils.isEmpty(children)) {
				for (Element child : children) {
					String name = child.getAttributeValue("name");
					String value = child.getText();
					parameters.put(name, value);
				}
			}
		}

		DefaultBatchJobMetadata batchJobMetadata = new DefaultBatchJobMetadata();
		batchJobMetadata.setComponentId(componentId);
		batchJobMetadata.setInitParameters(parameters);

		return batchJobMetadata;
	}

	/**
	 * 배치 트리거 메타데이터 정보 생성
	 * 
	 * @param element
	 * @return
	 */
	private IBatchTriggerMetadata parseBatchTriggerMetadata(Element element) {
		String type = StringUtils.defaultIfBlank(element.getAttributeValue("type"), "none").trim();
		TriggerType triggerType = TriggerType.of(type);

		Map<String, String> properties = new HashMap<>();
		Element propertiesEl = element.getChild("properties");
		if (propertiesEl != null) {
			List<Element> children = propertiesEl.getChildren();
			if (!CollectionUtils.isEmpty(children)) {
				for (Element child : children) {
					String name = child.getAttributeValue("name");
					String value = child.getText();
					properties.put(name, value);
				}
			}
		}

		DefaultBatchTriggerMetadata batchTriggerMetadata = new DefaultBatchTriggerMetadata();
		batchTriggerMetadata.setTriggerType(triggerType);
		batchTriggerMetadata.setProperties(properties);

		return batchTriggerMetadata;
	}

	@Override
	public void destroy() throws Exception {
		if (log.isInfoEnabled()) {
			log.info("# 프레임워크 배치 메타데이터 clear");
		}

		this.metadatas.clear();
	}
}
