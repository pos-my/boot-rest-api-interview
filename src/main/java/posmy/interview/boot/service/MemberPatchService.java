package posmy.interview.boot.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import posmy.interview.boot.enums.MemberPatchField;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.model.response.EmptyResponse;

@Service
public class MemberPatchService implements BaseService<MemberPatchRequest, EmptyResponse> {

    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public MemberPatchService(InMemoryUserDetailsManager inMemoryUserDetailsManager,
                              PasswordEncoder passwordEncoder) {
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EmptyResponse execute(MemberPatchRequest request) {
        MemberPatchField.lookup(request.getField())
                .patch(request, inMemoryUserDetailsManager, passwordEncoder);
        return new EmptyResponse();
    }
}
