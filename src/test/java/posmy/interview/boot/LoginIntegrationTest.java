package posmy.interview.boot;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import posmy.interview.boot.base.TestBase;
import posmy.interview.boot.comparator.RegexNullableValueMatcher;
import posmy.interview.boot.dto.UnitTestRequest;
import posmy.interview.boot.enums.HttpMethod;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginIntegrationTest extends TestBase {


    @Test
    @Order(10)
    public void When_withWrongPwd_Then_returnBadCredentials() {
        //wrong pwd
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/login?username=ck&password=abc")
                .responseFile("data/general/negative/badCredentialsResponse.json")
                .statusResult(status().isUnauthorized())
                .build());
    }

    @Test
    @Order(20)
    public void When_withWrongUser_Then_returnUserNotExists() {
        //not user exists
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/login?username=ck2&password=pa@@12.X9")
                .responseFile("data/user/negative/notExistsResponse.json")
                .statusResult(status().isUnauthorized())
                .build());
    }

    @Test
    @Order(30)
    public void When_loginWithCorrect_Then_loginAndReturnToken() {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/login?username=ck&password=pa@@12.X9")
                .responseFile("data/login/positive/loginResponse.json")
                .statusResult(status().isOk())
                .customComparator(getTokenComparator())
                .build());
    }


    public CustomComparator getTokenComparator() {
        // as long as id is numeric = fine, regardless under array or node
        return new CustomComparator(JSONCompareMode.STRICT
                , new Customization("access_token", new RegexNullableValueMatcher("[\\w-]*[.][\\w-]*[.][\\w-]*", false)
        ));
    }


}
