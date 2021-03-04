package posmy.interview.boot.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MemberPatchField;
import posmy.interview.boot.model.request.MemberPatchRequest;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.repos.MyUserRepository;

import javax.transaction.Transactional;

@Service
public class MemberPatchService implements BaseService<MemberPatchRequest, EmptyResponse> {

    private final MyUserRepository myUserRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberPatchService(MyUserRepository myUserRepository,
                              PasswordEncoder passwordEncoder) {
        this.myUserRepository = myUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public EmptyResponse execute(MemberPatchRequest request) {
        MyUser existingUser = findUserByIdOrThrow(request.getId());
        MyUser patchedUser = patchUser(existingUser, request);
        myUserRepository.save(patchedUser);
        return new EmptyResponse();
    }

    private MyUser findUserByIdOrThrow(Long id) {
        return myUserRepository.findById(id)
                .orElseThrow();
    }

    private MyUser patchUser(MyUser user, MemberPatchRequest request) {
        return MemberPatchField.lookup(request.getField())
                .patch(user, request.getValue(), passwordEncoder);
    }
}
