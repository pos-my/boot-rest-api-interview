package posmy.interview.boot.service;

import org.springframework.stereotype.Service;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.repos.MyUserRepository;

@Service
public class MemberSelfDeleteService implements BaseService<String, EmptyResponse> {

    private final MyUserRepository myUserRepository;

    public MemberSelfDeleteService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public EmptyResponse execute(String username) {
        myUserRepository.deleteByUsername(username);
        return new EmptyResponse();
    }
}
