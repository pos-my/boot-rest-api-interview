package posmy.interview.boot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;
import posmy.interview.boot.repositories.UserRepository;
import posmy.interview.boot.services.UserDetailsImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JWTUtils {
    private String jwtSecret = "'[a-zA-Z0-9]'";
    private String jwtCookie = "interviewCookie";

    private UserRepository userRepo;

    @Autowired
    public JWTUtils(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public String getJwtFromHeader(HttpServletRequest request) {
        if (request.getHeader("JWT Authorization") != null) {
            return request.getHeader("JWT Authorization").toString();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateJWTToken(userPrincipal);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/").maxAge(24 * 60 * 60).httpOnly(true).build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/").build();
        return cookie;
    }

    public String generateJWTToken(UserDetailsImpl userPrincipal) {
        String[] claims = getUserAuthorities(userPrincipal);

        return JWT.create().withIssuer("interview").withAudience("audience")
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .withSubject(userPrincipal.getUsername()).withArrayClaim("authorities", claims)
                .withExpiresAt(Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    public boolean validateToken(String token) throws Exception {
        return isTokenExpired(token);
    }

    private List<String> getAuthoritiesFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return (List<String>) verifier.verify(token).getClaim("authorities");
    }

    public String getUserNameFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algo = Algorithm.HMAC512(jwtSecret);
            verifier = JWT.require(algo).withIssuer("interview").build();
        } catch (JWTVerificationException e) {
            log.error(e.getMessage());
            throw new JWTVerificationException("Token could not be verified.");
        }
        return verifier;
    }

    private boolean isTokenExpired(String token) {
        JWTVerifier verifier = getJWTVerifier();
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private String[] getUserAuthorities(UserDetailsImpl userPrincipal) {
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority auth : userPrincipal.getAuthorities()){
            authorities.add(auth.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}