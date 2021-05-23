package posmy.interview.boot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.Users;
import posmy.interview.boot.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
