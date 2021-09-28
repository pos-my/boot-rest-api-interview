package posmy.interview.boot.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import posmy.interview.boot.exceptions.CustomRestApiException;
import posmy.interview.boot.models.daos.Book;
import posmy.interview.boot.models.daos.Member;
import posmy.interview.boot.models.dtos.book.BookStatus;
import posmy.interview.boot.models.dtos.member.UserRole;
import posmy.interview.boot.repositories.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemberServiceImplTest {

    private MemberServiceImpl memberService;
    private MemberRepository mockMemberRepository;
    private Member mockMember1;
    private Member mockMember2;
    private Optional<Member> mockMemberObject1;
    private Optional<Member> mockMemberObject2;
    private Optional<Member> mockMemberObject3;
    private List<Member> mockMemberList;

    @BeforeEach
    void setUp() {
        mockMemberRepository = mock(MemberRepository.class);
        memberService = new MemberServiceImpl(mockMemberRepository);
        mockMember1 = new Member();
        mockMember1.setMemberId(1);
        mockMember1.setFirstName("John");
        mockMember1.setLastName("Snow");
        mockMember1.setPhone("01234567");
        mockMember1.setEmail("john.snow@email.com");
        mockMember1.setUserRole(UserRole.LIBRARIAN);
        mockMemberObject1 = Optional.of(mockMember1);
        mockMember2 = new Member();
        mockMember2.setMemberId(2);
        mockMember2.setFirstName("Arya");
        mockMember2.setLastName("Stark");
        mockMember2.setPhone("11223344");
        mockMember2.setEmail("arya.stark@email.com");
        mockMember2.setUserRole(UserRole.MEMBER);
        mockMemberObject2 = Optional.of(mockMember2);
        mockMemberObject3 = Optional.empty();
        mockMemberList = new ArrayList<>();
        mockMemberList.add(mockMember1);
        mockMemberList.add(mockMember2);
    }

    @Test
    void getMemberById_exists_returnMember() throws Exception {
        long id = 1;
        when(mockMemberRepository.findById(id)).thenReturn(mockMemberObject1);
        var result = memberService.getMemberById(id);
        assertThat(result).isEqualTo(mockMember1);
    }

    @Test
    void getMemberById_doesNotExist_throwsException() {
        long id = 0;
        when(mockMemberRepository.findById(id)).thenReturn(mockMemberObject3);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            memberService.getMemberById(id);
        });
    }

    @Test
    void getAllMembers_membersExist_returnMembers() throws Exception {
        when(mockMemberRepository.findAll()).thenReturn(mockMemberList);
        var result = memberService.getAllMembers();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void getAllMembers_membersDoNotExist_throwsException() {
        when(mockMemberRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            memberService.getAllMembers();
        });
    }

    @Test
    void addMembers_success() throws Exception {
        Member memberRequest = new Member();
        memberRequest.setFirstName("Tyrion");
        memberRequest.setLastName("Lannister");
        memberRequest.setPhone("01901998");
        memberRequest.setEmail("tyrion.lannister@email.com");
        memberRequest.setUserRole(UserRole.MEMBER);
        List<Member> memberListRequest = new ArrayList<>();
        memberListRequest.add(memberRequest);
        when(mockMemberRepository.findByEmail(anyString())).thenReturn(mockMemberObject3);
        var result = memberService.addMembers(memberListRequest);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void addMembers_failure_throwsException() {
        List<Member> memberListRequest = new ArrayList<>();
        memberListRequest.add(mockMember1);
        when(mockMemberRepository.findByEmail(anyString())).thenReturn(mockMemberObject1);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            memberService.addMembers(mockMemberList);
        });
    }

    @Test
    void updateMemberById_success() throws Exception {
        long id = 1;
        Member memberRequest = mockMember2;
        memberRequest.setPhone("999");
        when(mockMemberRepository.findById(id)).thenReturn(mockMemberObject1);
        var result = memberService.updateMemberById(id, memberRequest);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void updateMemberById_failure_throwsException() {
        long id = 0;
        Member memberRequest = mockMember2;
        when(mockMemberRepository.findById(id)).thenReturn(mockMemberObject3);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            memberService.updateMemberById(id, memberRequest);
        });
    }

    @Test
    void removeMemberById_success() throws Exception {
        long id = 2;
        when(mockMemberRepository.findById(id)).thenReturn(mockMemberObject2);
        var result = memberService.removeMemberById(id);
        assertThat(result.getCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void removeMemberById_failure_throwsException() {
        long id = 0;
        when(mockMemberRepository.findById(id)).thenReturn(mockMemberObject3);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            memberService.removeMemberById(id);
        });
    }

    @Test
    void getMemberUserRole_success() throws Exception {
        String email = "arya.stark@email.com";
        when(mockMemberRepository.findByEmail(email)).thenReturn(mockMemberObject2);
        var result = memberService.getMemberUserRole(email);
        assertThat(result).isEqualTo(UserRole.MEMBER);
    }

    @Test
    void getMemberUserRole_failure_throwsException() {
        String email = "abc@email.com";
        when(mockMemberRepository.findByEmail(email)).thenReturn(mockMemberObject3);
        Assertions.assertThrows(CustomRestApiException.class, () -> {
            memberService.getMemberUserRole(email);
        });
    }
}