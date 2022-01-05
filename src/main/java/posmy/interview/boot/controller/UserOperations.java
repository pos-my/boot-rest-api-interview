package posmy.interview.boot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.user.*;

@RequestMapping("/users")
public interface UserOperations {
    @PostMapping("/create")
    RegistrationResponse createUser(@RequestBody RegistrationRequest registrationRequest);

    @PreAuthorize ("hasRole('ROLE_LIBRARIAN') || " + "hasRole('ROLE_MEMBER')")
    @GetMapping("/")
    GetUserResponse getUser(@RequestParam("pageSize") Integer pageSize,
                            @RequestParam("pageNumber") Integer pageNumber,
                            @RequestParam(value = "fullName", required = false) String fullName,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "id", required = false) Integer id);

    @PreAuthorize ("hasRole('ROLE_LIBRARIAN') || " + "hasRole('ROLE_MEMBER')")
    @PutMapping("/update")
    UpdateUserResponse updateUser(@RequestBody UpdateUserRequest updateUserRequest);

}
