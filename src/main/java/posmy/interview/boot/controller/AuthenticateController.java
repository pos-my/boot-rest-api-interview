package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.authentication.JwtRequest;
import posmy.interview.boot.authentication.JwtResponse;
import posmy.interview.boot.authentication.JwtTokenUtil;
import posmy.interview.boot.authentication.JwtUserDetailsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posmy")
public class AuthenticateController {

    private final AuthenticationManager authenticationManager;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> generateToken(@RequestBody JwtRequest jwtRequest) {
        authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());

        UserDetails userDetails = jwtUserDetailsService
            .loadUserByUsername(jwtRequest.getUsername());

        String token = jwtTokenUtil.generateToken(userDetails, jwtRequest.getExpiration());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            log.error(e.getMessage());
            throw new BadCredentialsException("Bad credential", e);
        }
    }
}
