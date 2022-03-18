package posmy.interview.boot.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.controller.request.TokenRefreshRequest;
import posmy.interview.boot.controller.request.UserAuthRequest;
import posmy.interview.boot.security.jwt.JwtResponse;
import posmy.interview.boot.security.jwt.TokenRefreshResponse;

import javax.validation.Valid;

@RequestMapping("/auth")
public interface AuthApi {

    @PostMapping(path = "/member/login")
    ResponseEntity<JwtResponse> memberLogin(@Valid @RequestBody UserAuthRequest request) throws Exception;

    @PostMapping(path = "/librarian/login")
    ResponseEntity<JwtResponse> librarianLogin(@Valid @RequestBody UserAuthRequest request) throws Exception;

    @PostMapping(path = "/refreshToken")
    ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request);

}
