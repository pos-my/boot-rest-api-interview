package posmy.interview.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.User;
import posmy.interview.boot.service.api.UserService;

import java.util.List;

@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public List<User> saveAll(@RequestBody List<User> users) {
        //for future proof, save using list
        // one stone kill all birds
        return userService.saveAll(users);
    }

    @GetMapping("/name/{name}")
    public User findByUsername(@PathVariable String name) {
        return userService.findByUsername(name);
    }

    @DeleteMapping("/me")
    public void removeMe() {
        userService.removeMe();
    }

    @DeleteMapping("/name/{name}")
    public void remove(@PathVariable String name) {
        userService.remove(name);
    }
}
