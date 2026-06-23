package com.scbank.process.api.svc.shared.components.sign.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.base.exception.PRCServiceException;
import com.scbank.process.api.fw.message.IMessageObject;
import com.scbank.process.api.svc.shared.components.sign.dto.SignVerifyInfo;

class SignUtilsTest {

    static class ChildMessage implements IMessageObject {
        private String childName;

        public String getChildName() {
            return childName;
        }

        public void setChildName(String childName) {
            this.childName = childName;
        }
    }

    static class TestMessage implements IMessageObject {
        private String name;
        private Integer age;
        private List<String> tags;
        private ChildMessage child;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public ChildMessage getChild() {
            return child;
        }

        public void setChild(ChildMessage child) {
            this.child = child;
        }
    }

    @Test
    @DisplayName("원문 문자열을 Map으로 변환")
    void getCompareMapFromOrgString_success() {

        String orgString = "name=kim&age=20&name=lee";

        Map<String, List<String>> result =
                SignUtils.getCompareMapFromOrgString(orgString);

        assertThat(result.get("name"))
                .containsExactly("kim", "lee");

        assertThat(result.get("age"))
                .containsExactly("20");
    }

    @Test
    @DisplayName("value가 없는 경우 빈 문자열 저장")
    void getCompareMapFromOrgString_emptyValue() {

        String orgString = "name=&age=20";

        Map<String, List<String>> result =
                SignUtils.getCompareMapFromOrgString(orgString);

        assertThat(result.get("name"))
                .containsExactly("");
    }

    @Test
    @DisplayName("IMessageObject를 비교용 Map으로 변환")
    void getCompareMapFromObject_success() {

        TestMessage msg = new TestMessage();
        msg.setName("kim");
        msg.setAge(20);

        SignVerifyInfo info = new SignVerifyInfo();
        info.setTranCd("T001");
        info.setImsTranCd("IMS001");
        info.setInClassCd("CLASS");
        info.setSvcCd("SVC");
        info.setJobTp("JOB");

        Map<String, List<String>> result =
                SignUtils.getCompareMapFromObject(msg, info);

        assertThat(result.get("name"))
                .containsExactly("kim");

        assertThat(result.get("age"))
                .containsExactly("20");

        assertThat(result.get("tranCd"))
                .containsExactly("T001");

        assertThat(result.get("imsTranCd"))
                .containsExactly("IMS001");
    }

    @Test
    @DisplayName("signVerifyInfo가 null이면 예외")
    void getCompareMapFromObject_signVerifyInfoNull() {

        TestMessage msg = new TestMessage();

        assertThatThrownBy(() ->
                SignUtils.getCompareMapFromObject(msg, null))
                .isInstanceOf(PRCServiceException.class);
    }

    @Test
    @DisplayName("IMessageObject 또는 Map이 아니면 예외")
    void getCompareMapFromObject_invalidObject() {

        SignVerifyInfo info = new SignVerifyInfo();

        assertThatThrownBy(() ->
                SignUtils.getCompareMapFromObject(new Object(), info))
                .isInstanceOf(PRCServiceException.class);
    }

    @Test
    @DisplayName("중첩 IMessageObject 수집")
    void collect_nestedObject() {

        ChildMessage child = new ChildMessage();
        child.setChildName("child");

        TestMessage parent = new TestMessage();
        parent.setName("parent");
        parent.setChild(child);

        Map<String, List<String>> result = new LinkedHashMap<>();

        SignUtils.collect(parent, result);

        assertThat(result.get("name"))
                .containsExactly("parent");

        assertThat(result.get("childName"))
                .containsExactly("child");
    }

    @Test
    @DisplayName("List 데이터 수집")
    void collect_list() {

        TestMessage msg = new TestMessage();
        msg.setTags(List.of("A", "B", "C"));

        Map<String, List<String>> result = new LinkedHashMap<>();

        SignUtils.collect(msg, result);

        assertThat(result.get("tags"))
                .containsExactly("A", "B", "C");
    }

    @Test
    @DisplayName("Map 데이터 수집")
    void collect_map() {

        Map<String, Object> source = new HashMap<>();
        source.put("name", "kim");
        source.put("age", 20);

        Map<String, List<String>> result = new LinkedHashMap<>();

        SignUtils.collect(source, result);

        assertThat(result.get("name"))
                .containsExactly("kim");

        assertThat(result.get("age"))
                .containsExactly("20");
    }

    @Test
    @DisplayName("최대 서명 데이터 건수 반환")
    void getTotalSignDataCount_success() {

        Map<String, List<String>> map = new HashMap<>();

        map.put("name", List.of("A", "B"));
        map.put("age", List.of("1", "2", "3"));

        Integer count = SignUtils.getTotalSignDataCount(map);

        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("빈 Map이면 0 반환")
    void getTotalSignDataCount_empty() {

        Integer count =
                SignUtils.getTotalSignDataCount(new HashMap<>());

        assertThat(count).isZero();
    }

    @Test
    @DisplayName("필드 조회 성공")
    void getFieldFromIMessageObject_success() {

        TestMessage msg = new TestMessage();
        msg.setName("kim");

        String result =
                SignUtils.getFieldFromIMessageObject(
                        msg,
                        "name",
                        String.class);

        assertThat(result).isEqualTo("kim");
    }

    @Test
    @DisplayName("존재하지 않는 필드는 null")
    void getFieldFromIMessageObject_notFound() {

        TestMessage msg = new TestMessage();

        String result =
                SignUtils.getFieldFromIMessageObject(
                        msg,
                        "notExist",
                        String.class);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("타입 불일치 시 null")
    void getFieldFromIMessageObject_typeMismatch() {

        TestMessage msg = new TestMessage();
        msg.setAge(20);

        String result =
                SignUtils.getFieldFromIMessageObject(
                        msg,
                        "age",
                        String.class);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("null 필드는 null 반환")
    void getFieldFromIMessageObject_nullField() {

        TestMessage msg = new TestMessage();

        String result =
                SignUtils.getFieldFromIMessageObject(
                        msg,
                        "name",
                        String.class);

        assertThat(result).isNull();
    }
}