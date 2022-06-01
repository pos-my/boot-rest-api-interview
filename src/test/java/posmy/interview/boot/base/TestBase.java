package posmy.interview.boot.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import posmy.interview.boot.Application;
import posmy.interview.boot.comparator.RegexNullableValueMatcher;
import posmy.interview.boot.dto.UnitTestRequest;
import posmy.interview.boot.dto.UnitTestResponse;
import posmy.interview.boot.dto.UserToken;
import posmy.interview.boot.enums.HttpMethod;
import posmy.interview.boot.enums.Role;
import posmy.interview.boot.util.TokenUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//todo: 1. implement run query to insert preload before running each UT
// + remove all created data to reset the data in future

/**
 * Mini version of unit test base
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {Application.class})
@Data
@Slf4j
public abstract class TestBase {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Value("${security.jwt.token.signkey}")
    private String signKey;

    List<UserToken> userTokens = new ArrayList<>();

    @BeforeAll
    public void beforeAll() {
        //add full access, librarian access and memeber access token
        //full access
        String accessToken = generateToken("ck", List.of("LIBRARIAN", "MEMBER"));
        addUserTokens(accessToken, Role.ALL);
        //librarian access
        accessToken = generateToken("staff", List.of("LIBRARIAN"));
        addUserTokens(accessToken, Role.LIBRARIAN);
        //member access
        accessToken = generateToken("member01", List.of("MEMBER"));
        addUserTokens(accessToken, Role.MEMBER);
    }

    private void addUserTokens(String accessToken, Role all) {
        userTokens.add(UserToken.builder()
                .accessToken(accessToken)
                .role(all)
                .build());
    }

    protected String generateToken(String username, List<String> roles) {
        return TokenUtil.generateToken(
                username,
                "unittest",
                roles,
                signKey, 120
        );
    }

    public String getTokenByAccess(Role role) {
        return userTokens.stream().filter(t -> t.getRole().equals(role)).findFirst().get().getAccessToken();
    }

    @SneakyThrows
    public UnitTestResponse getRequest(UnitTestRequest request) {
        //get request context, normally is json
        var requestContext = request.getRequestFile() == null
                ? null : Resources.toString(Resources.getResource(request.getRequestFile()), StandardCharsets.UTF_8);
        //massage the request Context by call the passing function
        if (request.getRequestContextInvoker() != null) {
            requestContext = request.getRequestContextInvoker().apply(requestContext);
        }
        //call post and set the request context etc
        MockHttpServletRequestBuilder builder = getMockHttpServletRequestBuilder(request, requestContext);
        //get the post result
        var result = mockMvc.perform(builder).andExpect(request.getStatusResult());
        //write the result as output for verify
        log.info("Verify actual result: {}", result.andReturn().getResponse().getContentAsString());


        //check is valid or not
        return checkJSONAssert(request, result);
    }

    public MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(
            UnitTestRequest request, String requestContext) {
        //get the correct builder
        var builder = request.getMethod() == HttpMethod.GET ?
                MockMvcRequestBuilders.get(request.getUrl())
                : request.getMethod() == HttpMethod.DELETE ?
                MockMvcRequestBuilders.delete(request.getUrl())
                :
                MockMvcRequestBuilders.post(request.getUrl());

        //set application json
        builder = builder.contentType(MediaType.APPLICATION_JSON);
        //set body content if exists
        if (requestContext != null) {
            builder = builder.content(requestContext);
        }
        //set bearer token if exists
        if (request.getToken() != null) {
            builder.header("Authorization", "Bearer " + request.getToken());
        }
        return builder;
    }

    @SneakyThrows
    private UnitTestResponse checkJSONAssert(UnitTestRequest request, ResultActions result) {
        return checkJSONAssert(request, result.andReturn().getResponse().getContentAsString());
    }

    @SneakyThrows
    public UnitTestResponse checkJSONAssert(UnitTestRequest request, String actualResponse) {
        try {
            //allow user set charset for the response file read
            var expectedResponse = request.getResponseFile() == null ? ""
                    : Resources.toString(Resources.getResource(request.getResponseFile())
                    , StandardCharsets.UTF_8);
            if (!actualResponse.isEmpty()) {
                if (request.getCustomComparator() == null) {
                    JSONAssert.assertEquals(expectedResponse, actualResponse, true);
                } else {
                    JSONAssert.assertEquals(expectedResponse, actualResponse, request.getCustomComparator());
                }
            }
            return UnitTestResponse.builder()
                    .actualResponse(actualResponse)
                    .build();
        } catch (Exception e) {
            log.info("Failed verify, actual result: {}", actualResponse);
            throw e;
        }

    }

    public CustomComparator getIdStandardComparator() {
        // as long as id is numeric = fine, regardless under array or node
        return new CustomComparator(JSONCompareMode.STRICT
                , new Customization("**.id", new RegexNullableValueMatcher("\\d+", false)
        ));
    }

    public void verifyInvalidToken(String url, HttpMethod method) {
        this.getRequest(UnitTestRequest.builder()
                .method(method)
                .url(url)
                .responseFile("data/general/negative/invalidTokenResponse.json")
                .statusResult(status().isUnauthorized())
                .build());
    }

    public void verifyAccessDenied(String url, HttpMethod method, Role role) {
        this.getRequest(UnitTestRequest.builder()
                .method(method)
                .url(url)
                .responseFile("data/general/negative/accessDeniedResponse.json")
                .statusResult(status().isUnauthorized())
                .token(this.getTokenByAccess(role))
                .build());
    }
}
