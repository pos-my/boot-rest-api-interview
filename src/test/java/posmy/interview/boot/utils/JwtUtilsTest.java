package posmy.interview.boot.utils;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import posmy.interview.boot.entity.UserIdAndRole;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;
import posmy.interview.boot.model.User;
import posmy.interview.boot.security.jwt.JwtUtils;
import posmy.interview.boot.security.service.UserDetailsImpl;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@RequiredArgsConstructor
public class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void generateToken()
    {
        UserDetailsImpl userDetails = UserDetailsImpl.buildUser(mockMember());
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void validateToken()
    {
        UserDetailsImpl userDetails = UserDetailsImpl.buildUser(mockMember());
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateLibrarianRole()
    {
        UserDetailsImpl userDetails = UserDetailsImpl.buildUser(mockLibrarian());
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        UserIdAndRole userIdAndRole = jwtUtils.getUserRoleFromJwtToken(token);
        assertEquals(UserRole.LIBRARIAN, userIdAndRole.getRole());
    }

    @Test
    void validateMemberRole()
    {
        UserDetailsImpl userDetails = UserDetailsImpl.buildUser(mockMember());
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);
        UserIdAndRole userIdAndRole = jwtUtils.getUserRoleFromJwtToken(token);
        assertEquals(UserRole.MEMBER, userIdAndRole.getRole());
    }

    private User mockMember(){
        User user = new User();
        user.setId(1L);
        user.setUsername("TestA");
        user.setPassword("passwordA");
        user.setRole(UserRole.MEMBER);
        user.setState(UserState.ACTIVE);
        return user;
    }

    private User mockLibrarian(){
        User user = new User();
        user.setId(1L);
        user.setUsername("TestA");
        user.setPassword("passwordA");
        user.setRole(UserRole.LIBRARIAN);
        user.setState(UserState.ACTIVE);
        return user;
    }
}
