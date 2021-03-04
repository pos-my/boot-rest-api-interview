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
import posmy.interview.boot.model.response.MemberGetResponse;
import posmy.interview.boot.service.MemberAddService;
import posmy.interview.boot.service.MemberDeleteService;
import posmy.interview.boot.service.MemberGetService;
import posmy.interview.boot.service.MemberPatchService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LibrarianAdminControllerTest {

    @Mock
    private MemberAddService memberAddService;
    @Mock
    private MemberPatchService memberPatchService;
    @Mock
    private MemberGetService memberGetService;
    @Mock
    private MemberDeleteService memberDeleteService;

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
        long id = 1L;
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(MemberPatchField.USER.name().toLowerCase())
                .value("newUser001")
                .build();
        MemberPatchRequest expectedRequest = request.toBuilder()
                .id(id)
                .build();

        mockMvc.perform(patch("/v1/librarian/admin/member/" + id)
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
        long id = 1L;
        MemberPatchRequest request = MemberPatchRequest.builder()
                .field(null)
                .value("newUser001")
                .build();

        mockMvc.perform(patch("/v1/librarian/admin/member/" + id)
                .characterEncoding(StandardCharsets.UTF_8.name())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is4xxClientError());
        verify(memberPatchService, times(0))
                .execute(any());
    }

    @Test
    void whenMemberGetThenReturnAllMembers() throws Exception {
        MemberGetResponse response = MemberGetResponse.builder()
                .members(List.of(new MemberGetResponse.UserDetailsDto(
                        1L, "user001", "abc@abc.com")))
                .build();

        when(memberGetService.execute(any()))
                .thenReturn(response);

        mockMvc.perform(get("/v1/librarian/admin/member")
                .characterEncoding(StandardCharsets.UTF_8.name())
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.members[0].id", equalTo(1)))
                .andExpect(jsonPath("$.members[0].username", equalTo("user001")))
                .andExpect(jsonPath("$.members[0].email", equalTo("abc@abc.com")));
        verify(memberGetService, times(1))
                .execute(any());
    }

    @Test
    void givenIdWhenMemberDeleteThenSuccess() throws Exception {
        long id = 12345L;

        mockMvc.perform(delete("/v1/librarian/admin/member/" + id)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        verify(memberDeleteService, times(1))
                .execute(id);
    }
}