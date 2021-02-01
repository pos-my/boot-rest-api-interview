package posmy.interview.boot;

import org.springframework.boot.test.mock.mockito.MockBean;
import posmy.interview.boot.repository.UsersRepository;

public class SecurityControllerTest {

    @MockBean
    private UsersRepository usersRepository;

}
