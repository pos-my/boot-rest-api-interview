package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Member;

import java.util.Optional;

@Service
public class MemberSelfService {

    @Autowired
    MemberService memberService;

    /**
     * Placeholder function to acquire member's own id
     *
     * @return dummy placeholder id
     */
    public Long getSelfId() {
        return 1L;
    }

    public Optional<Member> resign() {
        return memberService.delete(this.getSelfId());

    }
}
