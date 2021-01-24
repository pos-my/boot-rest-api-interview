package posmy.interview.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import posmy.interview.boot.persistence.Member;
import posmy.interview.boot.persistence.MemberRepository;
import posmy.interview.boot.persistence.Role;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class MemberRepositoryIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    private static final String NOOP = "{noop}";
    private final Member member = new Member("alex", Role.MEMBER, NOOP + "pwd");
    private final Member member2 = new Member("Amir", Role.LIBRARIAN, NOOP + "pwd1");

    @BeforeEach
    void setup() {
        memberRepository.deleteAll();
        memberRepository.saveAll(Arrays.asList(member, member2));
        memberRepository.flush();
    }

    @Test
    void whenFindAll_thenReturnAllMembers() {
        final List<Member> members = memberRepository.findAll();
        assertThat(members)
                .hasSize(2)
                .containsExactly(member, member2);
    }

    @Test
    void whenFindByName_thenReturnMember() {
        final Member actualMember = memberRepository.findByName(member.getName());
        final Member actualMember2 = memberRepository.findByName(member2.getName());

        assertThat(actualMember)
                .isNotNull()
                .isEqualTo(member);

        assertThat(actualMember2)
                .isNotNull()
                .isEqualTo(member2);
    }

    @Test
    void whenUpdate_thenReturnUpdatedMember() {
        member2.setRole(Role.MEMBER);
        memberRepository.saveAndFlush(member2);

        final Member actualMember2 = memberRepository.findByName(member2.getName());

        assertThat(actualMember2)
                .isNotNull()
                .isEqualTo(member2);
    }

    @Test
    void whenDeleteById_thenReturnNull() {
        memberRepository.deleteById(member2.getId());

        final Member actualMember2 = memberRepository.findByName(member2.getName());
        assertThat(actualMember2).isNull();
    }
}
