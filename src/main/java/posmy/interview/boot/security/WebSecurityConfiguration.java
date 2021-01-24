package posmy.interview.boot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import posmy.interview.boot.persistence.Role;

import java.util.Collections;

/**
 * @author Rashidi Zin
 */

// @Profile annotation indicates that a component is eligible for registration when one or more specified profiles are active
@Profile(Profiles.BASIC_AUTH)
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                // Only Librarian can perform add, update, view, and remove Member
                .antMatchers("/members/**").hasRole(Role.LIBRARIAN.name())
                // Only Librarian can perform add, update and remove Books
                // Librarian cannot modify the Book status
                .antMatchers(HttpMethod.GET, "/books/**").hasRole(Role.LIBRARIAN.name())
                .antMatchers(HttpMethod.POST, "/books/**").hasRole(Role.LIBRARIAN.name())
                .antMatchers(HttpMethod.PUT, "/books/**").hasRole(Role.LIBRARIAN.name())
                .antMatchers(HttpMethod.DELETE, "/books/**").hasRole(Role.LIBRARIAN.name())
                // Only Member can borrow and return book
                .antMatchers("/books/*/borrow", "/books/*/return").hasRole(Role.MEMBER.name())
                .and().httpBasic()
                // Permit all other request with authentication
                .and().authorizeRequests().anyRequest().authenticated()
                // We don't need sessions to be created.
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
