package posmy.interview.boot.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.entity.Member;
import posmy.interview.boot.helper.SHAHelper;
import posmy.interview.boot.repository.MemberRepository;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Service
public class MemberEntityService extends BaseEntityService<Member, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberEntityService.class);
    @Autowired
    private MemberRepository memberRepository;

    @Override
    protected PagingAndSortingRepository<Member, Long> getRepository() {
        return memberRepository;
    }

    @Transactional
    public Member create(Member member) {
        return super.create(member);
    }

    @Transactional
    public Member update(Member member) {
        return super.update(member);
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
    protected Member doUpdate(Member entity, Member changeSet) {

        if (isChanged(entity.getPhoneNumber(), changeSet.getPhoneNumber())) {
            entity.setPhoneNumber(changeSet.getPhoneNumber());
        }

        return entity;
    }

    @Override
    protected void doDelete(Member entity) {
        throw new UnsupportedOperationException("Not supported");
    }
}
