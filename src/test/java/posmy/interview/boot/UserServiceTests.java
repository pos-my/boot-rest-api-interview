package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import posmy.interview.boot.entity.RolesEntity;
import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.enums.Roles;
import posmy.interview.boot.model.CreateUserRequest;
import posmy.interview.boot.repository.RolesRepository;
import posmy.interview.boot.repository.UsersRepository;
import posmy.interview.boot.service.user.UserService;
import posmy.interview.boot.service.user.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    private UserService userService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    void initUserService() {
        userService = new UserServiceImpl(usersRepository, rolesRepository, modelMapper, bCryptPasswordEncoder);
    }

    @Test
    void should_return_created_member() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("test@gmail.com");
        createUserRequest.setFirstName("test");
        createUserRequest.setLastName("lastname");
        createUserRequest.setPassword("testing");
        createUserRequest.setUsername("whoami");
        createUserRequest.setRoles(Roles.MEMBER);
        when(modelMapper.map(createUserRequest, UsersEntity.class)).thenReturn(new UsersEntity());
        when(bCryptPasswordEncoder.encode(any(String.class))).thenReturn("someRandomString");
        when(usersRepository.findByUsername(any(String.class))).thenReturn(null);
        when(rolesRepository.findByName(any(String.class))).thenReturn(new RolesEntity());
        UsersEntity usersEntity = userService.registerNewUser(createUserRequest);
        assertThat(usersEntity).isNotNull();
    }

    @Test
    void should_return_user() throws Exception {
        UsersEntity expectedUserEntity = new UsersEntity();
        expectedUserEntity.setUsername("testUser");
        when(usersRepository.findById(any(Long.class))).thenReturn(Optional.of(expectedUserEntity));
        UsersEntity usersEntity = userService.getUser(any(Long.class));
        assertThat(usersEntity.getUsername()).isEqualTo(expectedUserEntity.getUsername());
    }
}
