package posmy.interview.boot.fixture;

import org.apache.commons.lang3.StringUtils;
import posmy.interview.boot.dto.IDto;
import posmy.interview.boot.dto.MemberCreateDto;
import posmy.interview.boot.dto.MemberUpdateDto;
import posmy.interview.boot.entity.Member;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class MemberBuilder implements IBuilder<Member, Long> {

    private Long id;

    private String username = randomAlphabetic(10);
    private String password = randomAlphabetic(10);
    private String phoneNumber;
    private String token;

    public static MemberBuilder sample() {
        return new MemberBuilder();
    }

    @Override
    public Member build() {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);

        if (StringUtils.isNotBlank(phoneNumber)) {
            member.setPhoneNumber(phoneNumber);
        }

        if (StringUtils.isNotBlank(token)) {
            member.setToken(token);
        }

        return member;
    }

    @Override
    public IDto buildDto() {
        return null;
    }

    @Override
    public MemberCreateDto buildCreateDto() {
        MemberCreateDto memberCreateDto = new MemberCreateDto();
        memberCreateDto.username = this.username;
        memberCreateDto.password = this.password;
        if (StringUtils.isNotBlank(phoneNumber)) {
            memberCreateDto.phoneNumber = phoneNumber;
        }
        return memberCreateDto;
    }

    @Override
    public MemberUpdateDto buildUpdateDto() {
        MemberUpdateDto memberUpdateDto = new MemberUpdateDto();
        if (StringUtils.isNotBlank(phoneNumber)) {
            memberUpdateDto.phoneNumber = phoneNumber;
        }
        return memberUpdateDto;
    }

    public MemberBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public MemberBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public MemberBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public MemberBuilder generateToken() {
        this.token = randomAlphabetic(32);
        return this;
    }
}
