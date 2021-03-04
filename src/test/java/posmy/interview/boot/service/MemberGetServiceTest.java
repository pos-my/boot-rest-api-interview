package posmy.interview.boot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.model.request.EmptyRequest;
import posmy.interview.boot.model.response.MemberGetResponse;
import posmy.interview.boot.repos.MyUserRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberGetServiceTest {

    private final MyUserRepository myUserRepository = mock(MyUserRepository.class);

    private final MemberGetService memberGetService = new MemberGetService(myUserRepository);

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberGetResponse expectedResponse;

    @BeforeEach
    void setup() {
        MyUser user1 = MyUser.builder()
                .id(1L)
                .username("user001")
                .password(passwordEncoder.encode("pass001"))
                .enabled(true)
                .authority(MyRole.MEMBER.authority)
                .email("abc001@abc.com")
                .createDt(ZonedDateTime.now().minusDays(1))
                .lastUpdateDt(ZonedDateTime.now().minusDays(1))
                .build();
        MyUser user2 = MyUser.builder()
                .id(2L)
                .username("user002")
                .password(passwordEncoder.encode("pass002"))
                .enabled(true)
                .authority(MyRole.MEMBER.authority)
                .email(null)
                .createDt(ZonedDateTime.now().minusDays(1))
                .lastUpdateDt(ZonedDateTime.now().minusDays(1))
                .build();
        when(myUserRepository.findAllByAuthority(MyRole.MEMBER.authority))
                .thenReturn(List.of(user1, user2));

        List<MemberGetResponse.UserDetailsDto> members = new ArrayList<>();
        MemberGetResponse.UserDetailsDto member1 = MemberGetResponse.UserDetailsDto.builder()
                .id(1L)
                .username("user001")
                .email("abc001@abc.com")
                .build();
        MemberGetResponse.UserDetailsDto member2 = MemberGetResponse.UserDetailsDto.builder()
                .id(2L)
                .username("user002")
                .build();
        members.add(member1);
        members.add(member2);
        expectedResponse = MemberGetResponse.builder()
                .members(members)
                .build();
    }

    @Test
    void whenMemberGetThenReturnAllMembers() {
        MemberGetResponse response = memberGetService.execute(new EmptyRequest());
        verify(myUserRepository, times(1))
                .findAllByAuthority(MyRole.MEMBER.authority);
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }
}