package posmy.interview.boot.service;

import io.vavr.control.Try;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.error.CreateDuplicateUserException;
import posmy.interview.boot.model.request.MemberAddRequest;
import posmy.interview.boot.model.response.EmptyResponse;

@Service
public class MemberAddService implements BaseService<MemberAddRequest, EmptyResponse> {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    public MemberAddService(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    @Override
    public EmptyResponse execute(MemberAddRequest request) {
        UserDetails userDetails = User.withUsername(request.getUser())
                .password(request.getPass())
                .roles(MyRole.MEMBER.name())
                .build();
        Try.run(() -> inMemoryUserDetailsManager.createUser(userDetails))
                .onFailure(IllegalArgumentException.class,
                        ex -> { throw new CreateDuplicateUserException(request.getUser(), ex); })
                .get();
        return new EmptyResponse();
    }
}
