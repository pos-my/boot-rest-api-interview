package posmy.interview.boot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import posmy.interview.boot.service.MemberSelfDeleteService;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberAdminControllerTest {

    @Mock
    private MemberSelfDeleteService memberSelfDeleteService;

    @InjectMocks
    private MemberAdminController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void whenMemberSelfDeleteThenDeleteByPrincipalUsername() throws Exception {
        Principal principal = mock(Principal.class);
        String username = "user001";
        when(principal.getName())
                .thenReturn(username);

        mockMvc.perform(delete("/v1/member/admin/self")
                .principal(principal)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        verify(memberSelfDeleteService, times(1))
                .execute(username);
    }
}