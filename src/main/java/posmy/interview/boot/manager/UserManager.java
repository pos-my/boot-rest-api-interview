package posmy.interview.boot.manager;

import org.springframework.data.domain.Page;
import posmy.interview.boot.domain.User;

import java.util.UUID;

public interface UserManager {
    User addMember(User user);

    User updateMember(User user);

    Page<User> getMemberList(int page, int size);

    User deleteMember(UUID id);

    User deleteOwnAccount(User user, String username);
}
