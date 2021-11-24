package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.manager.UserManager;

import java.security.Principal;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserManager userManager;

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/librarian/member")
    public ResponseEntity<?> addMember(@RequestBody User user) {
        try {
            User currUser = userManager.addMember(user);
            return new ResponseEntity<>(currUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/librarian/member")
    public ResponseEntity<?> updateMember(@RequestBody User user) {
        try {
            User currUser = userManager.updateMember(user);
            return new ResponseEntity<>(currUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/librarian/member")
    public ResponseEntity<?> getMemberList(@RequestParam int page, @RequestParam int size) {
        try {
            Page<User> currUserPage = userManager.getMemberList(page, size);
            return new ResponseEntity<>(currUserPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/librarian/member/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable("id") UUID id) {
        try {
            User currUser = userManager.deleteMember(id);
            return new ResponseEntity<>(currUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('MEMBER')")
    @PutMapping("/member")
    public ResponseEntity<?> deleteOwnAccount(@RequestBody User user, Principal principal) {
        try {
            User currUser = userManager.deleteOwnAccount(user, principal.getName());
            return new ResponseEntity<>(currUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
