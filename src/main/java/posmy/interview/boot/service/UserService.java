package posmy.interview.boot.service;

import org.springframework.data.domain.Page;
import posmy.interview.boot.entity.User;

public interface UserService {
    User addUser(User user);

    User updateUser(User user);

    User deleteUser(User user);

    Page<User> getMembers(int page);

    User getMemberByUsername(String username);

    User deleteOwnAccount(String username);
}
