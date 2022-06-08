package posmy.interview.boot.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.model.Role;
import posmy.interview.boot.repository.MemberRepository;

import javax.annotation.PostConstruct;

@Component
public class InitializeStartup {

    private final Logger log = LoggerFactory.getLogger(InitializeStartup.class);
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitializeStartup(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    public void initLibrarian(){
        log.info("init master librarian");
        Member masterLibrarian = new Member();
        masterLibrarian.setFirstName("zanget");
        masterLibrarian.setLastName("su");
        masterLibrarian.setUsername("zanget");
        masterLibrarian.setPassword(passwordEncoder.encode("test"));
        masterLibrarian.setRole(Role.ROLE_LIBRARIAN);
        memberRepository.save(masterLibrarian);
    }
}
