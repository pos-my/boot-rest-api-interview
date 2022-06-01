package posmy.interview.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import posmy.interview.boot.auth.CustomOncePerRequestFilter;
import posmy.interview.boot.auth.CustomUsernamePasswordAuthFilter;
import posmy.interview.boot.util.AuthUtil;

import static org.springframework.http.HttpMethod.*;
import static posmy.interview.boot.constant.Constant.LIBRARIAN;
import static posmy.interview.boot.constant.Constant.MEMBER;

@Configuration
public class SecurityConfig {
    @Value("${security.jwt.token.signkey}")
    private String signKey;
    @Value("${security.jwt.token.expiry.minute:60}")
    private Integer tokenExpiryMinute;

    /**
     * Use for auth's pwd encode
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Setup the http security
     *
     * @param http http security
     * @return security filter chain
     */
    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration
            , ObjectMapper mapper) {

        return http
                .httpBasic()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler(mapper))
                .and()
                //defined allowed API vs roles
                .authorizeRequests()
                //Librarian access
                .antMatchers(POST, "/book").hasAuthority(LIBRARIAN)
                .antMatchers(DELETE, "/book/name/**").hasAuthority(LIBRARIAN)
                .antMatchers(POST, "/user").hasAuthority(LIBRARIAN)
                .antMatchers(GET, "/user/name/**").hasAuthority(LIBRARIAN)
                .antMatchers(DELETE, "/user/name/**").hasAuthority(LIBRARIAN)
                .antMatchers(GET, "/role/all").hasAuthority(LIBRARIAN)
                //member access
                .antMatchers(GET, "/book/name/**").hasAuthority(MEMBER)
                .antMatchers(POST, "/book/borrow").hasAuthority(MEMBER)
                .antMatchers(POST, "/book/return").hasAuthority(MEMBER)
                .antMatchers(DELETE, "/user/me").hasAuthority(MEMBER)
                .and()
                //disable csrf to avoid attack of cross-site
                .csrf().disable()
                //for h2-console UI display
                .headers().frameOptions().disable()
                //add filter to verify user login
                .and().addFilter(new CustomUsernamePasswordAuthFilter(authenticationConfiguration.getAuthenticationManager(), signKey, mapper, tokenExpiryMinute))
                //add filter to verify user login request is valid
                .addFilterBefore(new CustomOncePerRequestFilter(signKey, mapper), CustomUsernamePasswordAuthFilter.class).build()
                ;
    }

    /**
     * Override forbidden message
     *
     * @return access denied handler
     */
    private AccessDeniedHandler accessDeniedHandler(ObjectMapper mapper) {
        return (request, response, ex) -> AuthUtil.setAuthExceptionResponse(response, mapper, ex.getMessage());
    }

}
