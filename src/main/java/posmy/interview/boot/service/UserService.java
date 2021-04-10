package posmy.interview.boot.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.dto.request.CreateUserDto;
import posmy.interview.boot.dto.request.UpdateUserDto;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.exception.NotFoundException;
import posmy.interview.boot.mapper.UserMapper;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper = UserMapper.getInstance();
    private PasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();

    @Transactional
    public User createUser(CreateUserDto createUserDto) {
        List<Role> roles = new ArrayList<>();
        createUserDto.getRoleIds().forEach(roleId -> roles.add(findRoleBy(roleId)));

        User user = userMapper.mapCreateUserDtoToEntity(
            createUserDto,
            bCryptEncoder.encode(createUserDto.getPassword()),
            roles
        );

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = findUserBy(id);

        List<Role> roles = new ArrayList<>();
        updateUserDto.getRoleIds().forEach(roleId -> roles.add(findRoleBy(roleId)));

        user.setUsername(updateUserDto.getUsername());
        user.setPassword(bCryptEncoder.encode(updateUserDto.getPassword()));
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserBy(Long id) {
        User user = findUserBy(id);
        userRepository.delete(user);
    }

    @Transactional
    public void deleteUserBy(String username) {
        User user = findUserBy(username);
        userRepository.delete(user);
    }

    private Role findRoleBy(Long id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Role not found for id: " + id));
    }

    private User findUserBy(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found for id: " + id));
    }

    private User findUserBy(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found for username: " + username));
    }
}
