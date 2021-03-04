package posmy.interview.boot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import posmy.interview.boot.repos.MyUserRepository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberSelfDeleteServiceTest {

    @Mock
    private MyUserRepository myUserRepository;

    @InjectMocks
    private MemberSelfDeleteService memberSelfDeleteService;

    @Test
    void whenSelfDeleteThenDeleteByUsername() {
        String username = "user001";
        memberSelfDeleteService.execute(username);
        verify(myUserRepository, times(1))
                .deleteByUsername(username);
    }
}