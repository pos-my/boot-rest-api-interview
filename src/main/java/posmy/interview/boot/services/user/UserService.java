package posmy.interview.boot.services.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import posmy.interview.boot.model.request.UserRequest;
import posmy.interview.boot.model.response.UserResponse;

import java.util.List;

public interface UserService {

    //Librarians
    void remove(String id); // can be used by members too
    List<UserResponse> viewAll();
    UserResponse view(String id);
    UserResponse save(UserRequest userRequest);
    UserResponse update(UserRequest userRequest);
}
