package posmy.interview.boot.Services.ServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.Exception.GenericException;
import posmy.interview.boot.Model.Repository.UsersRepository;
import posmy.interview.boot.Model.Users;
import posmy.interview.boot.Services.BookService;
import posmy.interview.boot.Services.MemberService;

import static java.util.Objects.nonNull;
import static posmy.interview.boot.Constant.M_UNABLE_DELETE_OTHERS_ACCOUNT;
import static posmy.interview.boot.Constant.M_USERNAME_NOT_FOUND;
import static posmy.interview.boot.Constant.UNABLE_DELETE_OTHERS_ACCOUNT;
import static posmy.interview.boot.Constant.USERNAME_NOT_FOUND;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private BookService bookService;
    @Autowired
    private UsersRepository usersRepository;


    @Override
    public Users addMember(String username, String password) {
        var member = Users.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode(password))
                .role("MEMBER")
                .enabled(1)
                .build();

        return usersRepository.save(member);
    }

    @Override
    public Users getMember(String username) {
        return findUserByUsername(username);
    }

    @Override
    public void updateMember(String oriUsername, String username, String password, int enabled, String role) {
        var member = findUserByUsername(oriUsername);

        member.setUsername((nonNull(username)) ? username : member.getUsername());
        member.setPassword((nonNull(password)) ? new BCryptPasswordEncoder().encode(password) : member.getPassword());
        member.setEnabled((nonNull(enabled)) ? enabled : member.getEnabled());
        member.setRole((nonNull(role)) ? role : member.getRole());

        usersRepository.save(member);
    }

    @Override
    public void deleteOwnAccount(String username) {
        if(!getCurrentUsername().equals(username)) {
            throw new GenericException(UNABLE_DELETE_OTHERS_ACCOUNT, M_UNABLE_DELETE_OTHERS_ACCOUNT);
        }

        var user = findUserByUsername(username);
        usersRepository.delete(user);
    }

    @Override
    public void deleteMemberAccount(String username) {
            var user = findUserByUsername(username);
            usersRepository.delete(user);
    }

    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    private Users findUserByUsername(String username) {
        return usersRepository.findByUsername(username).orElseThrow(() ->
                new GenericException(USERNAME_NOT_FOUND, M_USERNAME_NOT_FOUND));
    }
}
