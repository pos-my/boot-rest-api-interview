package posmy.interview.boot.service;

import posmy.interview.boot.model.User;

import java.util.List;

public interface UserService {
    List<User> viewMembers();
    User addMember(User user) throws Exception;
    User updateMember(User user);
    void removeMember(Integer userId);
}
