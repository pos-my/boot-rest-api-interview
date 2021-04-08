package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.dto.UserDto;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@Transactional
public class AccountControllerTest {

    private static final String ACCOUNT_API = "/api/v1/account";

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void userCreatesAccount() throws Exception {
        UserDto userDto = UserDto.builder().loginId("newmember66").name("New Member 66").pass("newmemberpass").build();
        mockMvc.perform(post(ACCOUNT_API + "/")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(not(nullValue()))))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.loginId", is("newmember66")))
                .andExpect(jsonPath("$.name", is("New Member 66")));
    }

    @WithMockUser(username = "member1", roles = "MEMBER")
    @Test
    public void memberDeletesAccount() throws Exception {
        mockMvc.perform(delete(ACCOUNT_API + "/"))
                .andExpect(status().isOk());
    }
}