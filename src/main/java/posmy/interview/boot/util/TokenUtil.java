package posmy.interview.boot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;

public class TokenUtil {
    public final static int ACCESS_TOKEN_EXP_MILIS = 10 * 60 * 1000;
    public final static int REFRESH_TOKEN_EXP_MILIS = 60 * 60 * 1000;

    public static DecodedJWT verifyToken(String token, String secret) throws JWTVerificationException, IllegalArgumentException {
        if(token.startsWith("Bearer ")){
            token = token.substring("Bearer ".length());
        }
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static String generateAccessToken(String username, String issuer, List<?> roles, String secret) throws IllegalArgumentException, JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXP_MILIS))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public static String generateRefreshToken(String username, String issuer, String secret) throws IllegalArgumentException, JWTCreationException {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXP_MILIS))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
