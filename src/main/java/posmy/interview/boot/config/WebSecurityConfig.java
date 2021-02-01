package posmy.interview.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import posmy.interview.boot.enums.Roles;
import posmy.interview.boot.service.user.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/api/book", "/api/member",  "/api/librarian").hasAnyAuthority(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.GET, "/api/book/**").hasAnyAuthority(Roles.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/api/book-record").hasAnyAuthority(Roles.MEMBER.name())
                .antMatchers(HttpMethod.DELETE, "/api/user").hasAnyAuthority(Roles.MEMBER.name())
                .antMatchers(HttpMethod.DELETE, "/api/user/**").hasAnyAuthority(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.PATCH, "/api/user").hasAnyAuthority(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                .antMatchers("/h2/**").permitAll()
                .anyRequest().authenticated();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
