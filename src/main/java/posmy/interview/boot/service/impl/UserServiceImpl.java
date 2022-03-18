package posmy.interview.boot.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.db.UserRepository;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserState;
import posmy.interview.boot.model.User;
import posmy.interview.boot.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAllMembers() {
        List<User> userList = new ArrayList<>();
        userRepository.findByRole(UserRole.MEMBER).forEach(userList::add);
        return userList;
    }

    @Override
    public User getUserByUserId(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.orElse(null);
    }

    @Override
    public User findActiveUser(String username, UserRole role) {
        return userRepository.findByUsernameAndRoleAndState(username, role, UserState.ACTIVE);
    }

    @Override
    public User addUser(String username, String encryptedPassword, UserRole role) throws Exception {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new Exception("User already exist");
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setRole(role);
        user.setState(UserState.ACTIVE);
        return userRepository.save(user);
    }

    @Override
    public User updateMember(User user) {
        Optional<User> requestUser = userRepository.findById(user.getId());
        if (requestUser.isEmpty()) {
            throw new ObjectNotFoundException("User not found. UserId : {}", String.valueOf(user.getId()));
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteMemberById(Long id) {
        Optional<User> requestUser = userRepository.findById(id);
        if (requestUser.isEmpty()) {
            throw new ObjectNotFoundException("User not found. UserId : {}", String.valueOf(id));
        }
        User user = requestUser.get();
        user.setState(UserState.DELETED);
        userRepository.save(user);
    }
}
