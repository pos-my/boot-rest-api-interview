package posmy.interview.boot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.entity.Member;
import posmy.interview.boot.helper.SHAHelper;
import posmy.interview.boot.repository.MemberRepository;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
public class MemberService extends BaseEntityService<Member, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
    @Autowired
    private MemberRepository memberRepository;

    @Override
    protected PagingAndSortingRepository<Member, Long> getRepository() {
        return memberRepository;
    }

    @PreAuthorize("hasAuthority('MEMBER_CREATE')")
    @Transactional
    public Member create(Member member) {
        return super.create(member);
    }

    @PreAuthorize("hasAuthority('MEMBER_UPDATE')")
    @Transactional
    public Member update(Long id, Member member) {
        member.setId(id);
        return super.update(member);
    }

    @PreAuthorize("hasAuthority('MEMBER_DELETE')")
    @Transactional
    public void delete(Long id) {
        super.delete(id);
    }

    @PreAuthorize("hasAuthority('SELF_DELETE')")
    @Transactional
    public void deleteSelf() {
        super.delete((Long) getAuthenticatedUser().getId());
    }

    @Override
    protected void doCreate(Member member) {
        member.setToken(randomAlphanumeric(32));

        try {
            member.setPassword(SHAHelper.hash(member.getPassword()));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            LOGGER.error("Error to encode due to {}", e.getMessage(), e);
            // wrap the error
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doUpdate(Member entity, Member changeSet) {

        if (isChanged(entity.getPhoneNumber(), changeSet.getPhoneNumber())) {
            entity.setPhoneNumber(changeSet.getPhoneNumber());
        }

    }

    @Override
    protected void doDelete(Member entity) {
    }

    @PreAuthorize("hasAuthority('MEMBER_READ')")
    @Transactional(readOnly = true)
    public Page<Member> get(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Member getByToken(String token) {
        return memberRepository.findByToken(token);
    }
}
