package posmy.interview.boot.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import posmy.interview.boot.model.MessageResponse;
import posmy.interview.boot.util.TokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.startsWithAny;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final String[] permitAllPaths = {"/login", "/user/token/refresh"};
    private final String secret;
    private final Gson gson;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Current servlet path {}", request.getRequestURI());
        if(startsWithAny(request.getRequestURI(), permitAllPaths)){
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader == null){
                String message = "Authorization header not found";
                log.error(message);
                response.setHeader("error", message);

                response.setStatus(UNAUTHORIZED.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                response.getOutputStream().write(
                        gson.toJson(
                                MessageResponse.builder()
                                        .status(UNAUTHORIZED.value())
                                        .message(message)
                                        .build()
                        ).getBytes());

            } else if(authorizationHeader.startsWith("Bearer ")) {
                try {
                    DecodedJWT decodedJWT = TokenUtil.verifyToken(authorizationHeader, secret);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    if(roles == null){
                        throw new RuntimeException("User roles not found");
                    }
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);

                } catch (Exception ex){
                    log.error("Error logging in: {}", ex.getMessage());
                    response.setHeader("error", ex.getMessage());

                    response.setStatus(UNAUTHORIZED.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    response.getOutputStream().write(
                            gson.toJson(
                                    MessageResponse.builder()
                                            .status(UNAUTHORIZED.value())
                                            .message(ex.getMessage())
                                            .build()
                            ).getBytes());
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
