package posmy.interview.boot.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.controller.request.AddUserRequest;
import posmy.interview.boot.model.User;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/user")
public interface UserApi {

    @GetMapping("/member/list")
    ResponseEntity<List<User>> fetchAllMembers();

    @PostMapping("/add")
    ResponseEntity<User> addUser(@Valid @RequestBody AddUserRequest request) throws Exception;

    @PutMapping("/member/update")
    ResponseEntity<User> updateMember(@RequestBody User user);

    @DeleteMapping("/member/delete/{userId}")
    ResponseEntity<Void> deleteMember(@PathVariable Long userId) throws Exception;
}
