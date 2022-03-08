package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.User;
import posmy.interview.boot.service.UserService;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("/viewMembers")
    public ResponseEntity<List<User>> viewMembers() {
        return ResponseEntity.ok(this.userService.viewMembers());
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("/addMember")
    public ResponseEntity<User> addMember(@RequestBody User user) throws Exception {
        return ResponseEntity.ok(this.userService.addMember(user));
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("/updateMember")
    public ResponseEntity<User> updateMember(@RequestBody User user) {
        return ResponseEntity.ok(this.userService.updateMember(user));
    }

    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_MEMBER')")
    @DeleteMapping("/removeMember/{userId}")
    public ResponseEntity<Void> removeMember(@PathVariable Integer userId) {
        this.userService.removeMember(userId);
        return ResponseEntity.noContent().build();
    }
}
