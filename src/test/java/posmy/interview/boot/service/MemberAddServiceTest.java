package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.error.CreateDuplicateUserException;
import posmy.interview.boot.model.request.MemberAddRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberAddServiceTest {

    @Spy
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

    @InjectMocks
    private MemberAddService memberAddService;

    @Captor
    private final ArgumentCaptor<UserDetails> userDetailsCaptor = ArgumentCaptor.forClass(UserDetails.class);

    private MemberAddRequest request;

    @BeforeEach
    void setup() {
        request = MemberAddRequest.builder()
                .user("user001")
                .pass("pass")
                .build();
    }

    @Test
    void whenMemberAddThenSuccess() {
        UserDetails expectedUser = User.withUsername(request.getUser())
                .password(request.getPass())
                .roles(MyRole.MEMBER.name())
                .build();

        memberAddService.execute(request);
        verify(inMemoryUserDetailsManager, times(1))
                .createUser(userDetailsCaptor.capture());
        assertThat(userDetailsCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(expectedUser);
    }

    @Test
    void givenDuplicateUserWhenMemberAddThenThrowCreateDuplicateUserException() {
        when(inMemoryUserDetailsManager.userExists(request.getUser()))
                .thenReturn(true);

        assertThrows(CreateDuplicateUserException.class,
                () -> memberAddService.execute(request));
        verify(inMemoryUserDetailsManager, times(1))
                .createUser(userDetailsCaptor.capture());
    }
}