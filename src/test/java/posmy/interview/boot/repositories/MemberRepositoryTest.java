package posmy.interview.boot.repositories;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import posmy.interview.boot.models.daos.Member;
import posmy.interview.boot.models.dtos.member.UserRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember1;
    private Member testMember2;
    private Member testMember3;

    @BeforeAll
    void setUp() {
        testMember1 = new Member();
        testMember1.setFirstName("John");
        testMember1.setLastName("Snow");
        testMember1.setPhone("12345678");
        testMember1.setEmail("john.snow@email.com");
        testMember1.setUserRole(UserRole.LIBRARIAN);
        memberRepository.save(testMember1);

        testMember2 = new Member();
        testMember2.setFirstName("Arya");
        testMember2.setLastName("Stark");
        testMember2.setPhone("11223344");
        testMember2.setEmail("arya.stark@email.com");
        testMember2.setUserRole(UserRole.MEMBER);
        memberRepository.save(testMember2);

        testMember3 = new Member();
        testMember3.setFirstName("Sansa");
        testMember3.setLastName("Stark");
        testMember3.setPhone("44556677");
        testMember3.setEmail("sansa.stark@email.com");
        testMember3.setUserRole(UserRole.MEMBER);
        memberRepository.save(testMember3);

    }

    @Test
    @Order(1)
    void findMemberById_MemberExist_ReturnMember() {
        long id = 1;
        var result = memberRepository.findById(id);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getFirstName()).isEqualTo(testMember1.getFirstName());
    }

    @Test
    @Order(2)
    void findMemberById_MemberDoesNotExist_ReturnEmpty() {
        long id = 999;
        var result = memberRepository.findById(id);
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @Order(3)
    void findAllMembers_ReturnMembers() {
        var result = memberRepository.findAll();
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @Order(4)
    void addNewMember_Success() {
        Member testMember = new Member();
        testMember.setFirstName("Tyrion");
        testMember.setLastName("Lannister");
        testMember.setPhone("99887766");
        testMember.setEmail("tyrion.lannister@email.com");
        testMember.setUserRole(UserRole.MEMBER);
        memberRepository.save(testMember);

        var result = memberRepository.findByEmail("tyrion.lannister@email.com");
        assertThat(result.get().getFirstName()).isEqualTo(testMember.getFirstName());
    }

    @Test
    @Order(5)
    void findByFirstName_ReturnMember() {
        var result = memberRepository.findByFirstName("John");
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(0).getEmail()).isEqualTo(testMember1.getEmail());
    }

    @Test
    @Order(6)
    void findByLastName_ReturnMember() {
        var result = memberRepository.findByLastName("Stark");
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @Order(7)
    void findByPhone_ReturnMember() {
        var result = memberRepository.findByPhone("11223344");
        assertThat(result.get().getFirstName()).isEqualTo(testMember2.getFirstName());
    }

    @Test
    @Order(8)
    void findByEmail_ReturnMember() {
        var result = memberRepository.findByEmail("sansa.stark@email.com");
        assertThat(result.get().getFirstName()).isEqualTo(testMember3.getFirstName());
    }

    @Test
    @Order(9)
    void findByUserRole_Member_ReturnMember() {
        var result = memberRepository.findByUserRole(UserRole.MEMBER);
        assertThat(result.size()).isEqualTo(2);
    }
}