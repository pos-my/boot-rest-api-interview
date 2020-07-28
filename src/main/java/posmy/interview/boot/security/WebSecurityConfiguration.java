package posmy.interview.boot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import posmy.interview.boot.service.UserDetailsServiceImpl;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
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
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
	}
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
            .antMatchers("/").hasAnyAuthority("LIBRARIAN", "MEMBER")
            .antMatchers("/view-books").hasAnyAuthority("LIBRARIAN", "MEMBER")
            .antMatchers("/view-users").hasAnyAuthority("LIBRARIAN", "MEMBER")
            .antMatchers("/create-book").hasAuthority("LIBRARIAN")
            .antMatchers("/create-user").hasAuthority("LIBRARIAN")
            .antMatchers("/update-book/**").hasAuthority("LIBRARIAN")
            .antMatchers("/update-user/**").hasAuthority("LIBRARIAN")
            .antMatchers(HttpMethod.POST, "/books/borrow/**").hasAuthority("MEMBER")
            .antMatchers(HttpMethod.POST, "/books/return/**").hasAuthority("MEMBER")
            .antMatchers(HttpMethod.POST, "/users/delete/**").hasAnyAuthority("LIBRARIAN", "MEMBER")
            .antMatchers(HttpMethod.POST, "/users/delete-all").hasAuthority("LIBRARIAN")
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling().accessDeniedPage("/403");
    }

}
