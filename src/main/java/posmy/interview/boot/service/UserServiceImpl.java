package posmy.interview.boot.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.dto.MyUserPrincipal;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.exception.UserNotFoundException;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.RoleRepository;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.system.Constant;
import posmy.interview.boot.system.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findFirstByLoginId(loginId).orElseThrow(() -> new UserNotFoundException(loginId));
        UserDto userDto = userMapper.convertToDto(user);
        return new MyUserPrincipal(userDto);
    }

    @Override
    public List<UserDto> findAllMembers() {
        List<User> users = userRepository.findByRoleName(Constant.UserRole.MEMBER.name());
        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto createMember(UserDto userDto) {
        User newMember = userMapper.convertToModel(userDto);
        List<Role> roleList = roleRepository.findFirstByName(Constant.UserRole.MEMBER.name()).stream().collect(Collectors.toList());
        newMember.setRoles(roleList);
        newMember.setPass(passwordEncoder.encode(userDto.getPass()));
        User createdMember = userRepository.save(newMember);
        return userMapper.convertToDto(createdMember);
    }

    @Override
    public UserDto updateMember(UserDto userDto, String loginId) {
        User member = userRepository.findFirstByLoginId(loginId).map(u -> {
            if (userDto.getName() != null) {
                u.setName(userDto.getName());
            }
            if (userDto.getPass() != null) {
                u.setPass(passwordEncoder.encode(userDto.getPass()));
            }
            return u;
        }).orElseThrow(() -> new UserNotFoundException(loginId));
        User updatedMember = userRepository.save(member);
        return userMapper.convertToDto(updatedMember);
    }

    @Override
    public void deleteMember(String loginId) {
        User member = userRepository.findFirstByLoginId(loginId).orElseThrow(() -> new UserNotFoundException(loginId));
        userRepository.delete(member);
    }
}
