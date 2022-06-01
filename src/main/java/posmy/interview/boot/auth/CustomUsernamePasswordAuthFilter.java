package posmy.interview.boot.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import posmy.interview.boot.dto.Token;
import posmy.interview.boot.util.AuthUtil;
import posmy.interview.boot.util.TokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String signKey;
    private final ObjectMapper mapper;
    private final Integer tokenExpiryMinute;

    public CustomUsernamePasswordAuthFilter(AuthenticationManager authenticationManager,
                                            String signKey, ObjectMapper mapper, Integer tokenExpiryMinute) {
        this.authenticationManager = authenticationManager;
        this.signKey = signKey;
        this.mapper = mapper;
        this.tokenExpiryMinute = tokenExpiryMinute;
    }

    @Override

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        //start auth
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("start to auth user: {}", username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        //when success login
        User user = (User) authResult.getPrincipal();
        log.info("successful auth: {}", user.getUsername());

        String accessToken = TokenUtil.generateToken(
                user.getUsername(),
                request.getRequestURL().toString(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                signKey, tokenExpiryMinute
        );

        response.setContentType(APPLICATION_JSON_VALUE);
        response.getOutputStream().write(
                mapper.writeValueAsBytes(Token.builder()
                        .accessToken(accessToken)
                        .build())
        );
        //todo: enhance include refresh token in future
    }

    @Override
    @SneakyThrows
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException failed) {
        //when fail login
        log.info("fail auth: {}", failed.getMessage());
        AuthUtil.setAuthExceptionResponse(response, mapper, failed.getMessage());
    }
}
