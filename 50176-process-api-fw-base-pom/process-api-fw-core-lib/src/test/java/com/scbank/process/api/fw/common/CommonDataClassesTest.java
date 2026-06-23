package com.scbank.process.api.fw.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.scbank.process.api.fw.common.code.ICodeItemInfo;
import com.scbank.process.api.fw.common.code.impl.DefaultCodeInfo;
import com.scbank.process.api.fw.common.code.impl.DefaultCodeItemInfo;
import com.scbank.process.api.fw.common.errorcode.impl.DefaultErrorButtonActionInfo;
import com.scbank.process.api.fw.common.errorcode.impl.DefaultErrorCodeInfo;
import com.scbank.process.api.fw.common.errorcode.impl.DefaultErrorGuideMessageInfo;
import com.scbank.process.api.fw.common.holiday.impl.DefaultHolidayInfo;
import com.scbank.process.api.fw.common.property.impl.DefaultPropertyInfo;
import com.scbank.process.api.fw.common.servicetime.ServiceTimeGroup;
import com.scbank.process.api.fw.common.servicetime.impl.DefaultServiceTimeInfo;

/**
 * common 패키지의 Lombok 기반 데이터/정보 구현 클래스들에 대한 단위 테스트.
 */
class CommonDataClassesTest {

    @Nested
    @DisplayName("DefaultPropertyInfo")
    class PropertyInfo {
        @Test
        void builderAndAccessors() {
            DefaultPropertyInfo info = DefaultPropertyInfo.builder()
                    .propertyName("k").propertyValue("v").build();

            assertThat(info.getPropertyName()).isEqualTo("k");
            assertThat(info.getPropertyValue()).isEqualTo("v");
            assertThat(info.toString()).contains("DefaultPropertyInfo");
        }
    }

    @Nested
    @DisplayName("DefaultHolidayInfo")
    class HolidayInfo {
        @Test
        void builderAndEquality() {
            DefaultHolidayInfo a = DefaultHolidayInfo.builder().date("20240101").description("New Year").build();
            DefaultHolidayInfo b = DefaultHolidayInfo.builder().date("20240101").description("New Year").build();

            assertThat(a.getDate()).isEqualTo("20240101");
            assertThat(a.getDescription()).isEqualTo("New Year");
            assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        }
    }

    @Nested
    @DisplayName("DefaultErrorGuideMessageInfo")
    class GuideMessageInfo {
        @Test
        void builderAndCompareTo() {
            DefaultErrorGuideMessageInfo first = DefaultErrorGuideMessageInfo.builder()
                    .message("a").order(1).build();
            DefaultErrorGuideMessageInfo second = DefaultErrorGuideMessageInfo.builder()
                    .message("b").order(2).build();

            assertThat(first.getMessage()).isEqualTo("a");
            assertThat(first.getOrder()).isEqualTo(1);
            // compareTo (default method) orders by 'order'
            assertThat(first.compareTo(second)).isNegative();
            assertThat(second.compareTo(first)).isPositive();
        }
    }

    @Nested
    @DisplayName("DefaultErrorButtonActionInfo")
    class ButtonActionInfo {
        @Test
        void builderAndAccessors() {
            DefaultErrorButtonActionInfo info = DefaultErrorButtonActionInfo.builder()
                    .type("LINK").label("이동").target("/home").build();

            assertThat(info.getType()).isEqualTo("LINK");
            assertThat(info.getLabel()).isEqualTo("이동");
            assertThat(info.getTarget()).isEqualTo("/home");
        }
    }

    @Nested
    @DisplayName("DefaultErrorCodeInfo")
    class ErrorCodeInfo {
        @Test
        void builderAndAccessors() {
            DefaultErrorGuideMessageInfo guide = DefaultErrorGuideMessageInfo.builder().message("g").order(0).build();
            DefaultErrorButtonActionInfo button = DefaultErrorButtonActionInfo.builder().type("OK").build();
            DefaultErrorCodeInfo info = DefaultErrorCodeInfo.builder()
                    .code("E001").langCode("ko").message("msg")
                    .errorGuideMessageInfos(List.of(guide))
                    .displayType("MODAL")
                    .errorButtonActionInfo(button)
                    .build();

            assertThat(info.getCode()).isEqualTo("E001");
            assertThat(info.getLangCode()).isEqualTo("ko");
            assertThat(info.getMessage()).isEqualTo("msg");
            assertThat(info.getErrorGuideMessageInfos()).containsExactly(guide);
            assertThat(info.getDisplayType()).isEqualTo("MODAL");
            assertThat(info.getErrorButtonActionInfo()).isSameAs(button);
        }
    }

    @Nested
    @DisplayName("DefaultCodeItemInfo / DefaultCodeInfo")
    class CodeInfo {
        @Test
        void codeItemBuilder() {
            DefaultCodeItemInfo item = DefaultCodeItemInfo.builder().key("M").value("Male").order(1).build();

            assertThat(item.getKey()).isEqualTo("M");
            assertThat(item.getValue()).isEqualTo("Male");
            assertThat(item.getOrder()).isEqualTo(1);
        }

        @Test
        void codeInfoBuilder() {
            ICodeItemInfo item = DefaultCodeItemInfo.builder().key("M").value("Male").order(1).build();
            DefaultCodeInfo info = DefaultCodeInfo.builder()
                    .key("GENDER").locale("ko").codeItemList(List.of(item)).build();

            assertThat(info.getKey()).isEqualTo("GENDER");
            assertThat(info.getLocale()).isEqualTo("ko");
            assertThat(info.getCodeItemList()).containsExactly(item);
        }
    }

    @Nested
    @DisplayName("DefaultServiceTimeInfo")
    class ServiceTimeInfo {
        @Test
        void builderAndAccessors() {
            DefaultServiceTimeInfo info = DefaultServiceTimeInfo.builder()
                    .group(ServiceTimeGroup.MENU).code("M001").chkYn("Y").description("d")
                    .nextPage("/np").nextPageParameter("a=1").startTime("0900").endTime("1800").build();

            assertThat(info.getGroup()).isEqualTo(ServiceTimeGroup.MENU);
            assertThat(info.getCode()).isEqualTo("M001");
            assertThat(info.getChkYn()).isEqualTo("Y");
            assertThat(info.getStartTime()).isEqualTo("0900");
            assertThat(info.getEndTime()).isEqualTo("1800");
        }
    }

    @Nested
    @DisplayName("ServiceTimeGroup")
    class ServiceTimeGroupEnum {
        @Test
        void ofKnownValues() {
            assertThat(ServiceTimeGroup.of("menu")).isEqualTo(ServiceTimeGroup.MENU);
            assertThat(ServiceTimeGroup.of("MESSAGE")).isEqualTo(ServiceTimeGroup.MESSAGE);
        }

        @Test
        void ofUnknownReturnsUnknown() {
            assertThat(ServiceTimeGroup.of("other")).isEqualTo(ServiceTimeGroup.UNKNOWN);
        }

        @Test
        void values() {
            assertThat(ServiceTimeGroup.values())
                    .containsExactly(ServiceTimeGroup.MENU, ServiceTimeGroup.MESSAGE, ServiceTimeGroup.UNKNOWN);
        }
    }
}
