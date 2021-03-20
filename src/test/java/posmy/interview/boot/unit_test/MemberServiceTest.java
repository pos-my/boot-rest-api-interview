package posmy.interview.boot.unit_test;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import posmy.interview.boot.BaseTest;
import posmy.interview.boot.entity.Member;
import posmy.interview.boot.fixture.MemberBuilder;
import posmy.interview.boot.mockauth.WithMockCustomUser;
import posmy.interview.boot.services.MemberService;

import javax.persistence.EntityNotFoundException;

import static org.junit.Assert.*;

@SpringBootTest
@ContextConfiguration
public class MemberServiceTest extends BaseTest {

    @Autowired
    private MemberService memberService;

    @WithMockCustomUser(authorities = {"MEMBER_CREATE"})
    @Test
    public void createTest() {
        Member member = memberService.create(MemberBuilder.sample().build());
        assertTrue(member.getId() > 0);
    }

    @WithMockCustomUser(authorities = {"MEMBER_CREATE","MEMBER_UPDATE"})
    @Test
    public void updateTest() {
        String updatePhoneNumber = "60133461582";
        Member member = memberService.create(MemberBuilder.sample().build());
        member.setPhoneNumber(updatePhoneNumber);
        member = memberService.update(member);
        assertEquals(updatePhoneNumber, member.getPhoneNumber());
    }

    @WithMockCustomUser(authorities = {"MEMBER_CREATE", "MEMBER_READ", "MEMBER_DELETE"})
    @Test
    public void deleteTest() {
        Member member = memberService.create(MemberBuilder.sample().build());
        memberService.delete(member.getId());
        try {
            memberService.getById(member.getId());
        } catch (EntityNotFoundException e) {
            return;
        }
        fail();
    }

}
