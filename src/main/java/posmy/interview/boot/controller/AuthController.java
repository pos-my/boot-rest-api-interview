package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.controller.api.AuthApi;
import posmy.interview.boot.controller.request.TokenRefreshRequest;
import posmy.interview.boot.controller.request.UserAuthRequest;
import posmy.interview.boot.controller.response.UserLoginResponse;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.model.RefreshToken;
import posmy.interview.boot.model.User;
import posmy.interview.boot.security.jwt.JwtResponse;
import posmy.interview.boot.security.jwt.JwtUtils;
import posmy.interview.boot.security.jwt.TokenRefreshResponse;
import posmy.interview.boot.security.service.UserDetailsImpl;
import posmy.interview.boot.security.service.UserDetailsServiceImpl;
import posmy.interview.boot.service.RefreshTokenService;
import posmy.interview.boot.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public ResponseEntity<JwtResponse> memberLogin(UserAuthRequest request) {
        User user = userService.findActiveUser(request.getUsername(), UserRole.MEMBER);
        if (user == null) {
            throw new AccessDeniedException("Username or password incorrect");
        }
        UserLoginResponse response = getUserLoginResponse(user, request.getPassword());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(response.getUserId());

        return ResponseEntity.ok().body(new JwtResponse(response.getJwt(), refreshToken.getToken(), response.getUserId(),
                response.getUsername(), response.getRole(), response.getState()));
    }

    @Override
    public ResponseEntity<JwtResponse> librarianLogin(UserAuthRequest request) {
        User user = userService.findActiveUser(request.getUsername(), UserRole.LIBRARIAN);
        if (user == null) {
            throw new AccessDeniedException("Username or password incorrect");
        }
        UserLoginResponse response = getUserLoginResponse(user, request.getPassword());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(response.getUserId());

        return ResponseEntity.ok().body(new JwtResponse(response.getJwt(), refreshToken.getToken(), response.getUserId(),
                response.getUsername(), response.getRole(), response.getState()));
    }

    @Override
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User user = userService.getUserByUserId(userId);
                    String token = jwtUtils.generateTokenFromUser(user);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new AccessDeniedException("Refresh token is not in database"));
        return null;
    }

    private UserLoginResponse getUserLoginResponse(User user, String rawPassword) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null
                        ,userDetails.getAuthorities());

        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            throw new AccessDeniedException("Username or password incorrect");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateToken(userDetailsImpl);
        String role = userDetails.getAuthorities().stream().findFirst()
                .orElseThrow(() -> new AccessDeniedException("Insufficient privilege")).getAuthority();

        UserLoginResponse response = new UserLoginResponse();
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setState(user.getState());
        response.setJwt(jwtToken);

        return response;
    }
}
