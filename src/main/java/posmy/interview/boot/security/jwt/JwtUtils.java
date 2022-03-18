package posmy.interview.boot.security.jwt;


import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.entity.UserIdAndRole;
import posmy.interview.boot.model.User;
import posmy.interview.boot.security.service.UserDetailsImpl;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUserPrincipal(userPrincipal);
    }

    public String generateTokenFromUser(User user) {
        return Jwts.builder().setSubject(user.getRole().toString() +":"+ user.getId()).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateTokenFromUserPrincipal(UserDetailsImpl userPrincipal) {
        return Jwts.builder().setSubject(userPrincipal.getRole().toString() +":"+ userPrincipal.getId()).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UserIdAndRole getUserRoleFromJwtToken(String token) {
        String subject = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        if (StringUtils.isNotBlank(subject)) {
            String[] splittedSubject = subject.split(":");
            if (splittedSubject != null && splittedSubject.length == 2) {
                UserIdAndRole userRole = new UserIdAndRole();
                userRole.setRole(UserRole.valueOf(splittedSubject[0]));
                userRole.setId(Long.parseLong(splittedSubject[1]));
                return userRole;
            }
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
