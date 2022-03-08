package posmy.interview.boot.service.impl;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.User;
import posmy.interview.boot.repository.UserRepository;
import posmy.interview.boot.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> viewMembers() {
        List<User> userList = new ArrayList<>();
        this.userRepository.findAll().forEach(userList::add);
        return userList;
    }

    @Override
    public User addMember(User user) throws Exception {
        Optional<User> requestUser = this.userRepository.findById(user.getUserId());
        if (requestUser.isPresent()) {
            throw new Exception("User already exist");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateMember(User user) {
        Optional<User> requestUser = this.userRepository.findById(user.getUserId());
        if (requestUser.isEmpty()) {
            throw new ObjectNotFoundException("User not found. UserId : {}", String.valueOf(user.getUserId()));
        }
        return this.userRepository.save(user);
    }

    @Override
    public void removeMember(Integer userId) {
        Optional<User> requestUser = this.userRepository.findById(userId);
        if (requestUser.isEmpty()) {
            throw new ObjectNotFoundException("User not found. UserId : {}", String.valueOf(userId));
        }
        this.userRepository.deleteById(userId);
    }
}
