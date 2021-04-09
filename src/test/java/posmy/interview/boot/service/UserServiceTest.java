package posmy.interview.boot.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.testutils.factories.RoleFactory;
import posmy.interview.boot.testutils.factories.UserFactory;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Should create a new user")
    void createUser() {
        User mockUser = UserFactory.getInstance().constructUser();
        Role mockRole = RoleFactory.getInstance().constructRole();

        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(mockRole));
        when(userRepository.save(any())).thenReturn(mockUser);

        userService.createUser(UserFactory.getInstance().constructCreateUserDto());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should update existing user")
    void updateUser() {
        User mockUser = UserFactory.getInstance().constructUser();
        Role mockRole = RoleFactory.getInstance().constructRole();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(mockRole));
        when(userRepository.save(any())).thenReturn(mockUser);

        userService.updateUser(1L, UserFactory.getInstance().constructUpdateUserDto());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user by id")
    void deleteUserById() {
        User mockUser = UserFactory.getInstance().constructUser();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

        userService.deleteUserBy(1L);

        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    @DisplayName("Should delete user by username")
    void deleteUserByUsername() {
        User mockUser = UserFactory.getInstance().constructUser();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(mockUser));

        userService.deleteUserBy("username");

        verify(userRepository, times(1)).delete(any(User.class));
    }
}
