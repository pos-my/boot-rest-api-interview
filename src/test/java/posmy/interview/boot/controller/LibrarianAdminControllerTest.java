package posmy.interview.boot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import posmy.interview.boot.enums.MemberPatchField;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.service.MemberAddService;
import posmy.interview.boot.service.MemberPatchService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LibrarianAdminControllerTest {

    @Mock
    private MemberAddService memberAddService;
    @Mock
    private MemberPatchService memberPatchService;

    @InjectMocks
    private LibrarianAdminController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void whenMemberAddThenSuccess() throws Exception {
        MemberAddRequest request = MemberAddRequest.builder()
                .user("user001")
                .pass("pass")
                .build();

        mockMvc.perform(post("/v1/librarian/admin/member")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        verify(memberAddService, times(1))
                .execute(request);
    }

    @Test
    void givenBlankUserWhenMemberAddThenError() throws Exception {
        MemberAddRequest request = MemberAddRequest.builder()
                .user("")
                .pass("pass")
                .build();

        mockMvc.perform(post("/v1/librarian/admin/member")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        verify(memberAddService, times(0))
                .execute(request);
    }

    @Test
    void givenBlankPassWhenMemberAddThenError() throws Exception {
        MemberAddRequest request = MemberAddRequest.builder()
                .user("user001")
                .pass(null)
                .build();

        mockMvc.perform(post("/v1/librarian/admin/member")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        verify(memberAddService, times(0))
                .execute(request);
    }

    @Test
    void whenMemberPatchThenSuccess() throws Exception {
        String user = "user001";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.USER.name().toLowerCase())
                .value("newUser001")
                .build();
        MemberPatchRequest expectedRequest = request.toBuilder()
                .user(user)
                .build();

        mockMvc.perform(patch("/v1/librarian/admin/member/" + user)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        verify(memberPatchService, times(1))
                .execute(expectedRequest);
    }

    @Test
    void givenBlankFieldWhenMemberPatchThenError() throws Exception {
        String user = "user001";
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(null)
                .value("newUser001")
                .build();

        mockMvc.perform(patch("/v1/librarian/admin/member/" + user)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        verify(memberPatchService, times(0))
                .execute(any());
    }
}