package posmy.interview.boot.IntegrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserStatus;
import posmy.interview.boot.repo.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {
    Logger logger = LoggerFactory.getLogger(UserControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Add member with Librarian role")
    @WithMockUser(roles = "LIBRARIAN")
    void testAddMember() throws Exception {
        String username = "member1";

        User user = User.builder().username(username).password("p@ssw0rd")
                .role(UserRole.MEMBER).status(UserStatus.ACTIVE).build();

        mockMvc.perform(post("/librarian/member")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());

        Optional<User> savedUser = userRepository.findUserByUsername(username);
        assertThat(savedUser.isPresent()).isEqualTo(true);
        if (savedUser.isPresent()) {
            User currUser = savedUser.get();
            assertThat(user.equals(currUser)).isTrue();
        }
    }

    @Test
    @DisplayName("Update member with Librarian role")
    @WithMockUser(roles = "LIBRARIAN")
    void testUpdateMember() throws Exception {
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        User user = User.builder().id(uuid).username("member2").password("p@ssw0rd")
                .role(UserRole.MEMBER).status(UserStatus.ACTIVE).createdDate(date).build();

        userRepository.save(user);

        user.setStatus(UserStatus.DELETED);
        user.setRole(UserRole.LIBRARIAN);

        mockMvc.perform(put("/librarian/member")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Optional<User> savedUser = userRepository.findById(uuid);
        assertThat(savedUser.isPresent()).isEqualTo(true);
        if (savedUser.isPresent()) {
            User currUser = savedUser.get();
            assertThat(currUser.getStatus()).isEqualTo(UserStatus.DELETED);
            assertThat(currUser.getRole()).isEqualTo(UserRole.LIBRARIAN);
        }
    }

    @Test
    @DisplayName("Get available member list with Librarian role")
    @WithMockUser(roles = "LIBRARIAN")
    void testGetMemberList() throws Exception {
        MvcResult result = mockMvc.perform(get("/librarian/member")
                .contentType("application/json")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn();
        logger.info("Result: {}", result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Delete member with Librarian role")
    @WithMockUser(roles = "LIBRARIAN")
    void testDeleteMember() throws Exception {
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        User user = User.builder().id(uuid).username("member3").password("p@ssw0rd")
                .role(UserRole.MEMBER).status(UserStatus.ACTIVE).createdDate(date).build();
        userRepository.save(user);

        mockMvc.perform(put("/librarian/member/{id}", uuid)
                .contentType("application/json"))
                .andExpect(status().isOk());

        Optional<User> deletedUser = userRepository.findById(uuid);
        assertThat(deletedUser.isPresent()).isEqualTo(true);
        if (deletedUser.isPresent()) {
            User softDeleteUser = deletedUser.get();
            assertThat(softDeleteUser.getStatus()).isEqualTo(UserStatus.DELETED);
        }
    }

    @Test
    @DisplayName("Delete own account with Member role")
    @WithMockUser(username = "member4", roles = "MEMBER")
    void testDeleteOwnAccount() throws Exception {
        UUID uuid = UUID.randomUUID();
        Date date = new Date();
        User user = User.builder().id(uuid).username("member4").password("p@ssw0rd")
                .role(UserRole.MEMBER).status(UserStatus.ACTIVE).createdDate(date).build();
        userRepository.save(user);

        mockMvc.perform(put("/member")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        Optional<User> deletedUser = userRepository.findById(uuid);
        assertThat(deletedUser.isPresent()).isEqualTo(true);
        if (deletedUser.isPresent()) {
            User softDeleteUser = deletedUser.get();
            assertThat(softDeleteUser.getStatus()).isEqualTo(UserStatus.DELETED);
        }
    }
}
