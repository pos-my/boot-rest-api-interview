package posmy.interview.boot.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MemberPatchField;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.error.InvalidMemberPatchFieldException;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.repos.MyUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberPatchServiceTest {

    private final MyUserRepository myUserRepository = mock(MyUserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final MemberPatchService memberPatchService =
            new MemberPatchService(myUserRepository, passwordEncoder);

    @Captor
    private final ArgumentCaptor<MyUser> userCaptor = ArgumentCaptor.forClass(MyUser.class);

    private MyUser existingUser;
    private MemberPatchRequest request;


    @BeforeEach
    void setup() {
        Long id = 1L;
        existingUser = MyUser.builder()
                .id(id)
                .username("user001")
                .password(passwordEncoder.encode("pass"))
                .authority(MyRole.MEMBER.authority)
                .build();
        request = MemberPatchRequest.builder()
                .id(id)
                .field(MemberPatchField.USER.name().toLowerCase())
                .value("newUser001")
                .build();
    }

    @AfterEach
    void teardown() {
    }

    @Test
    void givenFieldUserThenUpdateUsername() {
        MyUser expectedUser = existingUser.toBuilder()
                .username(request.getValue())
                .build();

        when(myUserRepository.findById(request.getId()))
                .thenReturn(Optional.of(existingUser));

        memberPatchService.execute(request);
        verify(myUserRepository, times(1))
                .findById(request.getId());
        verify(myUserRepository, times(1))
                .save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @Test
    void givenFieldPassThenUpdatePassword() {
        String newPass = "newPass999";
        request.setField(MemberPatchField.PASS.name());
        request.setValue(newPass);

        MyUser expectedUser = existingUser.toBuilder()
                .password(passwordEncoder.encode(request.getValue()))
                .build();

        when(myUserRepository.findById(request.getId()))
                .thenReturn(Optional.of(existingUser));

        memberPatchService.execute(request);
        verify(myUserRepository, times(1))
                .save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .usingRecursiveComparison().ignoringFields("password")
                .isEqualTo(expectedUser);
        assertTrue(passwordEncoder.matches(
                newPass,
                userCaptor.getValue().getPassword()));
    }

    @Test
    void givenFieldRoleThenUpdateRole() {
        MyRole newRole = MyRole.LIBRARIAN;
        request.setField(MemberPatchField.ROLE.name());
        request.setValue(newRole.name());

        MyUser expectedUser = existingUser.toBuilder()
                .authority(newRole.authority)
                .build();

        when(myUserRepository.findById(request.getId()))
                .thenReturn(Optional.of(existingUser));

        memberPatchService.execute(request);
        verify(myUserRepository, times(1))
                .save(userCaptor.capture());
        assertThat(userCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @Test
    void givenInvalidFieldThenThrow() {
        request.setField("invalid");

        assertThrows(InvalidMemberPatchFieldException.class,
                () -> memberPatchService.execute(request));
        verify(myUserRepository, times(0))
                .findByUsername(anyString());
    }

    @Test
    void givenInvalidRoleThenThrow() {
        String newRole = "invalid";
        request.setField(MemberPatchField.ROLE.name());
        request.setValue(newRole);

        when(myUserRepository.findById(request.getId()))
                .thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class,
                () -> memberPatchService.execute(request));
        verify(myUserRepository, times(0))
                .save(any());
    }
}