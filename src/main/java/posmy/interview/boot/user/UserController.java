package posmy.interview.boot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import posmy.interview.boot.exceptions.DuplicateRecordException;
import posmy.interview.boot.exceptions.RecordNotFoundException;
import posmy.interview.boot.exceptions.UnauthorizedException;
import posmy.interview.boot.user.request.NewUserRequest;
import posmy.interview.boot.user.request.UpdateUserRequest;
import posmy.interview.boot.entities.User;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<List<User>> viewAll() throws UnauthorizedException {
        return ResponseEntity.ok(this.userService.viewAll());
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> view(@PathVariable Long id) throws RecordNotFoundException, UnauthorizedException {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.view(id));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN', 'ROLE_MEMBER')")
    public ResponseEntity<?> delete(@PathVariable Long id) throws RecordNotFoundException, UnauthorizedException {
        userService.delete(id);
        return ResponseEntity.ok("");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> add(@RequestBody NewUserRequest userRequest) throws DuplicateRecordException {
        return ResponseEntity.ok(this.userService.add(userRequest));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateUserRequest userRequest)
            throws RecordNotFoundException, UnauthorizedException {
        return ResponseEntity.ok(this.userService.update(id, userRequest));

    }
}