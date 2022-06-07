package posmy.interview.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.repository.MemberRepository;

@Service
public class LibraryUserDetailsService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Autowired
    public LibraryUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username);
    }
}
