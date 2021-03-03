package posmy.interview.boot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import posmy.interview.boot.enums.MemberPatchField;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.error.InvalidMemberPatchFieldException;
import posmy.interview.boot.model.request.MemberPatchRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberPatchServiceTest {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager = mock(InMemoryUserDetailsManager.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final MemberPatchService memberPatchService =
            new MemberPatchService(inMemoryUserDetailsManager, passwordEncoder);

    @Captor
    private final ArgumentCaptor<UserDetails> userDetailsCaptor = ArgumentCaptor.forClass(UserDetails.class);

    private UserDetails existingUser;
    private MemberPatchRequest request;


    @BeforeEach
    void setup() {
        String user = "user001";
        existingUser = User.withUsername(user)
                .passwordEncoder(passwordEncoder::encode)
                .password("pass")
                .roles(MyRole.MEMBER.name())
                .build();
        request = MemberPatchRequest.builder()
                .user(user)
                .field(MemberPatchField.USER.name().toLowerCase())
                .value("newUser001")
                .build();
    }

    @AfterEach
    void teardown() {
    }

    @Test
    void givenFieldUserThenUpdateUsername() {
        UserDetails expectedUser = User.withUserDetails(existingUser)
                .username(request.getValue())
                .build();

        when(inMemoryUserDetailsManager.loadUserByUsername(request.getUser()))
                .thenReturn(existingUser);

        memberPatchService.execute(request);
        verify(inMemoryUserDetailsManager, times(1))
                .loadUserByUsername(request.getUser());
        verify(inMemoryUserDetailsManager, times(1))
                .deleteUser(request.getUser());
        verify(inMemoryUserDetailsManager, times(1))
                .createUser(userDetailsCaptor.capture());
        assertThat(userDetailsCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @Test
    void givenFieldPassThenUpdatePassword() {
        String newPass = "newPass999";
        request.setField(MemberPatchField.PASS.name());
        request.setValue(newPass);

        UserDetails expectedUser = User.withUserDetails(existingUser)
                .passwordEncoder(passwordEncoder::encode)
                .password(request.getValue())
                .build();

        when(inMemoryUserDetailsManager.loadUserByUsername(request.getUser()))
                .thenReturn(existingUser);

        memberPatchService.execute(request);
        verify(inMemoryUserDetailsManager, times(1))
                .updateUser(userDetailsCaptor.capture());
        assertThat(userDetailsCaptor.getValue())
                .usingRecursiveComparison().ignoringFields("password")
                .isEqualTo(expectedUser);
        assertTrue(passwordEncoder.matches(
                newPass,
                userDetailsCaptor.getValue().getPassword()));
    }

    @Test
    void givenFieldRoleThenUpdateRole() {
        String newRole = MyRole.LIBRARIAN.name();
        request.setField(MemberPatchField.ROLE.name());
        request.setValue(newRole);

        UserDetails expectedUser = User.withUserDetails(existingUser)
                .roles(newRole)
                .build();

        when(inMemoryUserDetailsManager.loadUserByUsername(request.getUser()))
                .thenReturn(existingUser);

        memberPatchService.execute(request);
        verify(inMemoryUserDetailsManager, times(1))
                .updateUser(userDetailsCaptor.capture());
        assertThat(userDetailsCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @Test
    void givenInvalidFieldThenThrow() {
        request.setField("invalid");

        assertThrows(InvalidMemberPatchFieldException.class,
                () -> memberPatchService.execute(request));
        verify(inMemoryUserDetailsManager, times(0))
                .loadUserByUsername(anyString());
    }

    @Test
    void givenInvalidRoleThenThrow() {
        String newRole = "invalid";
        request.setField(MemberPatchField.ROLE.name());
        request.setValue(newRole);

        when(inMemoryUserDetailsManager.loadUserByUsername(request.getUser()))
                .thenReturn(existingUser);

        assertThrows(IllegalArgumentException.class,
                () -> memberPatchService.execute(request));
        verify(inMemoryUserDetailsManager, times(0))
                .updateUser(any());
    }
}