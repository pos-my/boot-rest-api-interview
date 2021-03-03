package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.error.CreateDuplicateUserException;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.repos.MyUserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberAddServiceTest {

    private final MyUserRepository myUserRepository = mock(MyUserRepository.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final MemberAddService memberAddService = new MemberAddService(myUserRepository, passwordEncoder);

    @Captor
    private final ArgumentCaptor<MyUser> myUserCaptor = ArgumentCaptor.forClass(MyUser.class);

    private MemberAddRequest request;

    @BeforeEach
    void setup() {
        request = MemberAddRequest.builder()
                .user("user001")
                .pass("pass")
                .email("abc@abc.co")
                .build();
    }

    @Test
    void whenMemberAddThenSuccess() {
        MyUser expectedUser = MyUser.builder()
                .username(request.getUser())
                .password(passwordEncoder.encode(request.getPass()))
                .authority(MyRole.MEMBER.authority)
                .email(request.getEmail())
                .build();

        when(myUserRepository.existsByUsername(request.getUser()))
                .thenReturn(false);

        memberAddService.execute(request);
        verify(myUserRepository, times(1))
                .save(myUserCaptor.capture());
        assertThat(myUserCaptor.getValue())
                .usingRecursiveComparison().ignoringFields("password")
                .isEqualTo(expectedUser);
        assertTrue(passwordEncoder.matches(
                request.getPass(),
                myUserCaptor.getValue().getPassword()));
    }

    @Test
    void givenDuplicateUserWhenMemberAddThenThrowCreateDuplicateUserException() {
        when(myUserRepository.existsByUsername(request.getUser()))
                .thenReturn(true);

        assertThrows(CreateDuplicateUserException.class,
                () -> memberAddService.execute(request));
        verify(myUserRepository, times(0))
                .save(myUserCaptor.capture());
    }
}