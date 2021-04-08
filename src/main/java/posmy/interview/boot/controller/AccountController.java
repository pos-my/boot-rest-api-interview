package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.dto.UserDto;
import posmy.interview.boot.exception.UserAlreadyExistsException;
import posmy.interview.boot.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController extends BaseController {

    @Autowired
    private UserService userService;

    @PreAuthorize("permitAll()")
    @PostMapping("/")
    public UserDto createAccount(@RequestBody UserDto userDto) {
        UserDto existingUser = userService.findByLoginId(userDto.getLoginId());
        if (existingUser != null) {
            throw new UserAlreadyExistsException(userDto.getLoginId());
        }
        setCreatedByAndDate(userDto, userDto.getLoginId());
        return userService.createMember(userDto);
    }

    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @DeleteMapping("/")
    public void deleteAccount(Principal principal) {
        userService.deleteMember(principal.getName());
    }
}
