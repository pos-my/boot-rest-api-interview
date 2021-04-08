package posmy.interview.boot.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import posmy.interview.boot.dto.UserDto;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserDto> findAllMembers();

    UserDto findByLoginId(String loginId);

    UserDto createMember(UserDto userDto);

    UserDto updateMember(UserDto userDto, String loginId);

    void deleteMember(String loginId);
}
