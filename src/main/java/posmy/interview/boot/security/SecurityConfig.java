package posmy.interview.boot.security;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import posmy.interview.boot.filter.CustomAuthenticationFilter;
import posmy.interview.boot.filter.CustomAuthorizationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final String secret;
    private final Gson gson;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder,
                          @Value("${security.authentication.secret}") String secret,
                          Gson gson) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.secret = secret;
        this.gson = gson;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.authorizeRequests().antMatchers(GET, "/user/token/refresh/**").permitAll();

        http.authorizeRequests().antMatchers(GET, "/book/get/**").hasAnyAuthority("LIBRARIAN", "MEMBER");
        http.authorizeRequests().antMatchers(GET, "/book/get-all-books/**").hasAnyAuthority("LIBRARIAN", "MEMBER");

        http.authorizeRequests().antMatchers(POST, "/book/save/**").hasAuthority("LIBRARIAN");
        http.authorizeRequests().antMatchers(DELETE, "/book/remove/**").hasAuthority("LIBRARIAN");
        http.authorizeRequests().antMatchers(GET, "/user/**").hasAuthority("LIBRARIAN");
        http.authorizeRequests().antMatchers(POST, "/user/**").hasAuthority("LIBRARIAN");
        http.authorizeRequests().antMatchers(DELETE, "/user/remove/**").hasAuthority("LIBRARIAN");

        http.authorizeRequests().antMatchers(DELETE, "/user/self-remove/**").hasAuthority("MEMBER");
        http.authorizeRequests().antMatchers(POST, "/book/borrow/**").hasAuthority("MEMBER");
        http.authorizeRequests().antMatchers(POST, "/book/return/**").hasAuthority("MEMBER");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(this.authenticationManagerBean(), secret, gson));
        http.addFilterBefore(new CustomAuthorizationFilter(secret, gson), UsernamePasswordAuthenticationFilter.class);
    }
}
