package posmy.interview.boot.controller;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.helper.ValidationHelper;
import posmy.interview.boot.model.request.UserRequest;
import posmy.interview.boot.model.response.UserResponse;
import posmy.interview.boot.services.user.UserService;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/viewAll")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<List<UserResponse>> viewAll(){
        return ResponseEntity.ok(this.userService.viewAll());
    }

    @GetMapping("/view")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> view(@RequestParam String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.view(id));
        } catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN', 'ROLE_MEMBER')")
    public ResponseEntity<?> delete(@RequestParam String id){

        try{
            userService.remove(id);
            return ResponseEntity.ok().body("User with id = " + id + " is removed.");
        }catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> add(@RequestBody UserRequest userRequest){
        try {
            ValidationHelper.isEmailValid(userRequest.getEmail());
            return ResponseEntity.ok(this.userService.save(userRequest));
        } catch (InvalidPropertiesFormatException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email");
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> update(@RequestBody UserRequest userRequest){
        try{
            ValidationHelper.isEmailValid(userRequest.getEmail());
            return ResponseEntity.ok(this.userService.update(userRequest));
        } catch (ObjectNotFoundException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        } catch (InvalidPropertiesFormatException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Email");
        }
    }
}
