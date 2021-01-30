package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.exception.LmsException;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateUserRequest;
import posmy.interview.boot.model.UpdateUserRequest;
import posmy.interview.boot.model.User;
import posmy.interview.boot.service.user.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UsersEntity> createNewUser(@RequestBody @Valid CreateUserRequest createUserRequest) throws LmsException {
        return ResponseEntity.ok(userService.registerNewUser(createUserRequest));
    }

    @DeleteMapping
    public ResponseEntity<UsersEntity> deleteAccount() throws NoDataFoundException {
        return ResponseEntity.ok(userService.deleteAccount());
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<UsersEntity> deleteAccountById(@PathVariable long userId) throws NoDataFoundException, LmsException {
        return ResponseEntity.ok(userService.deleteMemberAccount(userId));
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<User> getMemberDetails(@PathVariable long userId) throws NoDataFoundException, LmsException {
        return ResponseEntity.ok(userService.getMemberDetails(userId));
    }

    @PatchMapping
    public ResponseEntity<User> updateMemberDetails(@RequestBody @Valid UpdateUserRequest updateUserRequest) throws NoDataFoundException, LmsException {
        return ResponseEntity.ok(userService.updateMemberDetails(updateUserRequest));
    }
}
