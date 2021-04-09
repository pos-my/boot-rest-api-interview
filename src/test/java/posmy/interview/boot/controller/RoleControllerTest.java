package posmy.interview.boot.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static posmy.interview.boot.utils.CommonConstants.AUTHORIZATION_HEADER;
import static posmy.interview.boot.utils.CommonConstants.BEARER_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import posmy.interview.boot.authentication.JwtRequest;
import posmy.interview.boot.authentication.JwtResponse;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.service.RoleService;
import posmy.interview.boot.testutils.factories.RoleFactory;

@SpringBootTest
class RoleControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Should get a list of role")
    void shouldNotGetListOfRoleWithoutValidToken() throws Exception {
        List<Role> mockRoles = RoleFactory.getInstance().constructListOfRole();

        when(roleService.getRoles()).thenReturn(mockRoles);

        mockMvc
            .perform(
                get("/api/posmy/roles")
            )
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.responseCode", is(HttpStatus.UNAUTHORIZED.value())));

        verify(roleService, times(0)).getRoles();
    }

    @Test
    @DisplayName("Should get a list of role")
    void shouldGetListOfRole() throws Exception {
        List<Role> mockRoles = RoleFactory.getInstance().constructListOfRole();

        when(roleService.getRoles()).thenReturn(mockRoles);

        mockMvc
            .perform(
                get("/api/posmy/roles")
                    .header(AUTHORIZATION_HEADER, obtainJwtToken())
            ).andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.[0].id", is(1)));

        verify(roleService, times(1)).getRoles();
    }

    private String obtainJwtToken() throws Exception {
        ResultActions result = mockMvc.perform(
            post("/api/posmy/token")
                .content(objectMapper.writeValueAsString(
                    JwtRequest.builder().username("librarian").password("password").expiration(1000)
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is2xxSuccessful());

        String resultString = result.andReturn().getResponse().getContentAsString();
        JwtResponse response = objectMapper.readValue(resultString, JwtResponse.class);

        return BEARER_TOKEN + response.getToken();
    }
}
