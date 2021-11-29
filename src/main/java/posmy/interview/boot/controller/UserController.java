package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> addUser(@RequestBody User user) {
        try {
            User response = userService.addUser(user);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> updateUser(@RequestBody User user) {
        try {
            User response = userService.updateUser(user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> deleteUser(@RequestBody User user) {
        try {
            User response = userService.deleteUser(user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view/all")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> viewMembers(@RequestParam int page) {
        try {
            Page<User> response = userService.getMembers(page);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Object> viewMember(@RequestParam String username) {
        try {
            User response = userService.getMemberByUsername(username);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/delete/own")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<Object> deleteOwn(Authentication authentication) {
        try {
            User response = userService.deleteOwnAccount(authentication.getName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
