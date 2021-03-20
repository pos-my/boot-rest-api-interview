package posmy.interview.boot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.entity.UserAware;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class LoginService {

    @Autowired
    private LibrarianService librarianService;
    @Autowired
    private MemberService memberService;


    @Transactional(readOnly = true)
    public UserAware login(String token) {
        UserAware users = librarianService.getByToken(token);
        if (users == null) {
            users = memberService.getByToken(token);
        }
        return users;
    }

    @Transactional(readOnly = true)
    public UserAware loginBasicAuthentication(String authorization) {
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        final String[] values = credentials.split(":", 2);
        return login(values[0], values[1]);
    }

    @Transactional(readOnly = true)
    public UserAware<Long> login(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet");
    }
}
