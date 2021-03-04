package posmy.interview.boot.service;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.response.EmptyResponse;
import posmy.interview.boot.repos.MyUserRepository;

@Slf4j
@Service
public class MemberDeleteService implements BaseService<Long, EmptyResponse> {

    private final MyUserRepository myUserRepository;

    public MemberDeleteService(MyUserRepository myUserRepository) {
        this.myUserRepository = myUserRepository;
    }

    @Override
    public EmptyResponse execute(Long id) {
        Try.run(() -> myUserRepository.deleteById(id))
                .recover(EmptyResultDataAccessException.class,
                        ex -> warnAndSuppress(id, ex))
                .get();
        return new EmptyResponse();
    }

    private Void warnAndSuppress(Long id, Throwable ex) {
        log.warn("Trying to delete non-existing User with ID: " + id, ex);
        return null;
    }
}
