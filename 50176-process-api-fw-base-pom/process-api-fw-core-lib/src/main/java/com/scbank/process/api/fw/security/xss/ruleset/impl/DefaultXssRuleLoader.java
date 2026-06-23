package com.scbank.process.api.fw.security.xss.ruleset.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.scbank.process.api.fw.core.utils.StringUtils;
import com.scbank.process.api.fw.security.xss.ruleset.IXssRuleLoader;
import com.scbank.process.api.fw.security.xss.ruleset.XssRuleInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JDOM 기반 XML 파일을 통해 XSS 룰셋을 로딩하는 기본 구현체입니다.
 * <p>
 * Spring의 {@link PathMatchingResourcePatternResolver}를 사용하여 classpath 상의 룰셋 XML
 * 파일을 로딩하며,
 * XML 내 <code>&lt;rule&gt;</code> 요소들을 {@link XssRuleInfo} 객체로 파싱하여 리스트로 반환합니다.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultXssRuleLoader implements IXssRuleLoader {

    private static final String DEFAULT_RULESET_LOCATION = "classpath:config/xss/xss-ruleset.xml";
    /**
     * 룰셋 XML의 위치 (classpath 경로 또는 file: 경로 등)
     */
    private final String location;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<XssRuleInfo> load() {
        return this.load(location);
    }

    /**
     * 지정한 경로의 XML에서 룰셋을 로딩합니다.
     *
     * @param xmlPath classpath 기준 XML 경로
     * @return XSS 룰 정보 리스트
     */
    private List<XssRuleInfo> load(String xmlPath) {

        if (StringUtils.isEmpty(xmlPath)) {
            xmlPath = DEFAULT_RULESET_LOCATION;
        }

        List<XssRuleInfo> rules = new ArrayList<>();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(xmlPath);

        try (InputStream is = resource.getInputStream()) {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(is);
            Element root = document.getRootElement(); // <ruleset>

            List<Element> ruleElements = root.getChildren("rule");
            for (Element ruleEl : ruleElements) {
                String target = ruleEl.getChildTextTrim("target");
                String replacement = ruleEl.getChildTextTrim("replacement");

                XssRuleInfo rule = new XssRuleInfo();
                rule.setTarget(target);
                rule.setReplacement(replacement);
                rules.add(rule);
            }
        } catch (Exception e) {
            log.error("XSS 룰셋 로딩 중 오류 발생: {}", xmlPath, e);
        }

        return rules;
    }
}
