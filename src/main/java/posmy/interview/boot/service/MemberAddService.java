package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.error.CreateDuplicateUserException;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.repos.MyUserRepository;

@Service
public class MemberAddService implements BaseService<MemberAddRequest, EmptyResponse> {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberAddService(MyUserRepository myUserRepository,
                            @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmptyResponse execute(MemberAddRequest request) {
        validateDuplicateUser(request.getUser());
        MyUser user = buildMemberUser(request);
        myUserRepository.save(user);
        return new EmptyResponse();
    }

    private void validateDuplicateUser(String username) {
        if (myUserRepository.existsByUsername(username))
            throw new CreateDuplicateUserException(username);
    }

    private MyUser buildMemberUser(MemberAddRequest request) {
        return MyUser.builder()
                .username(request.getUser())
                .password(passwordEncoder.encode(request.getPass()))
                .authority(MyRole.MEMBER.authority)
                .email(request.getEmail())
                .build();
    }
}
