package posmy.interview.boot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import posmy.interview.boot.security.UUIDAuthenticationService;
import posmy.interview.boot.security.UserAuthenticationService;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class LoginController {
	@NonNull
	  UserAuthenticationService authentication;

	@PostMapping("login")
	String login(@RequestParam("username") final String username, @RequestParam("password") final String password) {
		return authentication.login(username, password)
				.orElseThrow(() -> new RuntimeException("invalid login and/or password"));
	}
}
