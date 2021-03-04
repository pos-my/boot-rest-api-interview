package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import posmy.interview.boot.repos.MyUserRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberDeleteServiceTest {

    private final MyUserRepository myUserRepository = mock(MyUserRepository.class);

    private final MemberDeleteService memberDeleteService =
            new MemberDeleteService(myUserRepository);

    @BeforeEach
    void setup() {

    }

    @Test
    void givenIdThenDeleteById() {
        Long id = 123L;

        memberDeleteService.execute(id);
        verify(myUserRepository, times(1))
                .deleteById(id);
    }

    @Test
    void givenNonExistIdThenDoNothing() {
        Long id = 123L;

        doThrow(new EmptyResultDataAccessException(1))
                .when(myUserRepository).deleteById(id);

        memberDeleteService.execute(id);
        verify(myUserRepository, times(1))
                .deleteById(id);
    }

    @Test
    void whenOtherExceptionThenThrow() {
        Long id = 123L;

        doThrow(new DataAccessResourceFailureException("Fail to connect datasource"))
                .when(myUserRepository).deleteById(id);

        assertThrows(DataAccessResourceFailureException.class,
                () -> memberDeleteService.execute(id));
        verify(myUserRepository, times(1))
                .deleteById(id);
    }
}