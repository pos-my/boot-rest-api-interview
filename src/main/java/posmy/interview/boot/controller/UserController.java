package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.controller.api.CurrentUserDetailsApi;
import posmy.interview.boot.controller.api.UserApi;
import posmy.interview.boot.controller.request.AddUserRequest;
import posmy.interview.boot.model.User;
import posmy.interview.boot.security.service.UserDetailsImpl;
import posmy.interview.boot.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements CurrentUserDetailsApi, UserApi {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<User>> fetchAllMembers() {
        return ResponseEntity.ok(this.userService.findAllMembers());
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<User> addUser(@Valid @RequestBody AddUserRequest request) throws Exception {
        return ResponseEntity.ok(this.userService.addUser(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getRole()));
    }

    @Override
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<User> updateMember(User user) {
        return ResponseEntity.ok(this.userService.updateMember(user));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('MEMBER', 'LIBRARIAN')")
    public ResponseEntity<Void> deleteMember(Long userId) throws Exception {
        UserDetailsImpl userDetails = getUserDetails();
        if (userDetails.getId() != userId) {
            throw new Exception("Unable to delete other user");
        }
        this.userService.deleteMemberById(userId);
        return ResponseEntity.noContent().build();
    }
}
