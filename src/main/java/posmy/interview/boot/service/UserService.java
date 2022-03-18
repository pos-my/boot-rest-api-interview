package posmy.interview.boot.service;

import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllMembers();

    User getUserByUserId(Long id);

    User findActiveUser(String username, UserRole role);

    User addUser(String username, String encryptedPassword, UserRole role) throws Exception ;

    User updateMember(User user);

    void deleteMemberById(Long id);
}
