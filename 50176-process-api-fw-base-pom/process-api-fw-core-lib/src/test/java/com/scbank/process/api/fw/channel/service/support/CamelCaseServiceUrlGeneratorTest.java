package com.scbank.process.api.fw.channel.service.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.scbank.process.api.fw.channel.service.annotation.ServiceEndpoint;
import com.scbank.process.api.fw.core.component.ServiceComponent;

/**
 * CamelCaseServiceUrlGenerator Test Class
 */
@ExtendWith(MockitoExtension.class)
class CamelCaseServiceUrlGeneratorTest {

    private CamelCaseServiceUrlGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new CamelCaseServiceUrlGenerator();
    }

    @Nested
    @DisplayName("generate tests")
    class GenerateTests {

        @Test
        @DisplayName("Should generate URL from class name when url is not specified")
        void shouldGenerateUrlFromClassNameWhenUrlNotSpecified() throws Exception {
            Method method = TestUserService.class.getMethod("login");

            String result = generator.generate(TestUserService.class, method);

            assertEquals("/test/user/login", result);
        }

        @Test
        @DisplayName("Should use annotation url when specified on class")
        void shouldUseAnnotationUrlWhenSpecifiedOnClass() throws Exception {
            Method method = TestServiceWithUrl.class.getMethod("action");

            String result = generator.generate(TestServiceWithUrl.class, method);

            assertEquals("/custom/url/action", result);
        }

        @Test
        @DisplayName("Should use annotation url when specified on method")
        void shouldUseAnnotationUrlWhenSpecifiedOnMethod() throws Exception {
            Method method = TestServiceWithMethodUrl.class.getMethod("customAction");

            String result = generator.generate(TestServiceWithMethodUrl.class, method);

            assertEquals("/test/service/with/method/url/custom-action", result);
        }

        @Test
        @DisplayName("Should remove Service suffix from class name")
        void shouldRemoveServiceSuffixFromClassName() throws Exception {
            Method method = UserService.class.getMethod("getUser");

            String result = generator.generate(UserService.class, method);

            assertEquals("/user/getUser", result);
        }

        @Test
        @DisplayName("Should remove Svc suffix from class name")
        void shouldRemoveSvcSuffixFromClassName() throws Exception {
            Method method = UserSvc.class.getMethod("getUser");

            String result = generator.generate(UserSvc.class, method);

            assertEquals("/user/getUser", result);
        }

        @Test
        @DisplayName("Should remove _SVC suffix from class name")
        void shouldRemoveUnderscoreSVCSuffixFromClassName() throws Exception {
            Method method = User_SVC.class.getMethod("getUser");

            String result = generator.generate(User_SVC.class, method);

            assertEquals("/user/getUser", result);
        }

        @Test
        @DisplayName("Should handle single word class name")
        void shouldHandleSingleWordClassName() throws Exception {
            Method method = Account.class.getMethod("details");

            String result = generator.generate(Account.class, method);

            assertEquals("/account/details", result);
        }

        @Test
        @DisplayName("Should add leading slash to method url if not present")
        void shouldAddLeadingSlashToMethodUrlIfNotPresent() throws Exception {
            Method method = TestUserService.class.getMethod("login");

            String result = generator.generate(TestUserService.class, method);

            assertTrue(result.startsWith("/"));
        }
    }

    // Test classes
    @ServiceComponent(id = "testUser")
    public static class TestUserService {
        @ServiceEndpoint(url = "", name = "login", description = "Login")
        public void login() {}
    }

    @ServiceComponent(id = "testWithUrl", url = "/custom/url")
    public static class TestServiceWithUrl {
        @ServiceEndpoint(url = "", name = "action", description = "Action")
        public void action() {}
    }

    @ServiceComponent(id = "testWithMethodUrl")
    public static class TestServiceWithMethodUrl {
        @ServiceEndpoint(url = "/custom-action", name = "customAction", description = "Custom Action")
        public void customAction() {}
    }

    @ServiceComponent(id = "userService")
    public static class UserService {
        @ServiceEndpoint(url = "", name = "getUser", description = "Get User")
        public void getUser() {}
    }

    @ServiceComponent(id = "userSvc")
    public static class UserSvc {
        @ServiceEndpoint(url = "", name = "getUser", description = "Get User")
        public void getUser() {}
    }

    @ServiceComponent(id = "user_SVC")
    public static class User_SVC {
        @ServiceEndpoint(url = "", name = "getUser", description = "Get User")
        public void getUser() {}
    }

    @ServiceComponent(id = "account")
    public static class Account {
        @ServiceEndpoint(url = "", name = "details", description = "Details")
        public void details() {}
    }
}
