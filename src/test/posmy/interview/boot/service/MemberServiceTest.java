package posmy.interview.boot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import posmy.interview.boot.model.Member;
import posmy.interview.boot.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService = new MemberService();


    @DisplayName("Test view member list")
    @Test
    void testViewAvailable() {
        Mockito.when(memberRepository.findAll()).thenReturn(List.of(
                Member.builder().id(1L).deleted(false).build(),
                Member.builder().id(2L).deleted(true).build()
        ));
        List<Member> sampleMemberList = memberService.viewAll();
        Assertions.assertEquals(2, sampleMemberList.size());
    }

    @DisplayName("Test view empty member list")
    @Test
    void testViewEmptyAvailable() {

        List<Member> sampleMemberList = memberService.viewAll();
        Assertions.assertEquals(0, sampleMemberList.size());
    }

    @DisplayName("Test get member")
    @Test
    void testGetMember() {
        Mockito.when(memberRepository.findById(12L))
                .thenReturn(Optional.of(
                        Member.builder().id(12L).deleted(false).build()
                ));
        Optional<Member> maybeMember = memberService.get(12L);
        Assertions.assertTrue(maybeMember.isPresent());
        Assertions.assertFalse(maybeMember.get().isDeleted());
    }

    @DisplayName("Test get non-existing member")
    @Test
    void testGetNonExistMember() {

        Optional<Member> maybeMember = memberService.get(13L);
        Assertions.assertTrue(maybeMember.isEmpty());
    }


    @DisplayName("Test add new member")
    @Test
    void testAddNewMember() {
        Member newMember = Member.builder()
                .id(1L)
                .deleted(false)
                .build();
        Mockito.when(memberRepository.save(Mockito.any(Member.class)))
                .thenReturn(newMember);

        Member createdMember = memberService.add(newMember);

        Assertions.assertNotNull(createdMember);
        Assertions.assertFalse(createdMember.isDeleted());
    }

    @DisplayName("Test update member")
    @Test
    void testUpdateMember() {

        Member changeMember = Member.builder()
                .id(1L)
                .deleted(true)
                .build();
        Mockito.when(memberRepository.save(changeMember))
                .thenReturn(changeMember);

        Member updateMember = memberService.update(changeMember);

        Assertions.assertNotNull(updateMember);
        Assertions.assertTrue(updateMember.isDeleted());
    }

    @DisplayName("Test delete member")
    @Test
    void testDeleteMember() {

        Member currentMember = Member.builder()
                .id(1L)
                .deleted(false)
                .build();
        Member changeMember = Member.builder()
                .id(1L)
                .deleted(true)
                .build();
        Mockito.when(memberRepository.findById(1L))
                .thenReturn(Optional.of(currentMember));
        Mockito.when(memberRepository.save(changeMember))
                .thenReturn(changeMember);

        Optional<Member> updateMember = memberService.delete(1L);

        Assertions.assertNotNull(updateMember);
        Assertions.assertTrue(updateMember.isPresent());
        Assertions.assertTrue(updateMember.get().isDeleted());
    }

    @DisplayName("Test delete deleted member")
    @Test
    void testDeleteDeletedMember() {

        Mockito.when(memberRepository.findById(2L))
                .thenReturn(Optional.of(
                        Member.builder()
                                .id(2L)
                                .deleted(true)
                                .build()
                ));


        Optional<Member> updateMember = memberService.delete(2L);

        Assertions.assertNotNull(updateMember);
        Assertions.assertTrue(updateMember.isPresent());
        Assertions.assertTrue(updateMember.get().isDeleted());
    }

    @DisplayName("Test delete non-existing member")
    @Test
    void testDeleteNonExistMember() {

        Optional<Member> updateMember = memberService.delete(3L);

        Assertions.assertNotNull(updateMember);
        Assertions.assertTrue(updateMember.isEmpty());
    }

}
