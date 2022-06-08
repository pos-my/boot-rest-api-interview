package posmy.interview.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import posmy.interview.boot.service.LibraryUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private LibraryUserDetailsService libraryUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().and()
                .authorizeRequests()
                .antMatchers("/userManagement/user").access("hasRole('ROLE_LIBRARIAN')")
                .antMatchers("/userManagement/user/all").access("hasRole('ROLE_LIBRARIAN')")
                .antMatchers(HttpMethod.GET, "/bookManagement/book").access("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_MEMBER')")
                .antMatchers(HttpMethod.GET, "/bookManagement/book/all").access("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_MEMBER')")
                .antMatchers(HttpMethod.POST, "/bookManagement/book").access("hasRole('ROLE_LIBRARIAN')")
                .antMatchers(HttpMethod.DELETE, "/bookManagement/book").access("hasRole('ROLE_LIBRARIAN')")
                .antMatchers(HttpMethod.PUT, "/bookManagement/book").access("hasRole('ROLE_LIBRARIAN')")
                .antMatchers("/borrow/book").access("hasRole('ROLE_LIBRARIAN') or hasRole('ROLE_MEMBER')")
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(libraryUserDetailsService);
        return provider;
    }
}
