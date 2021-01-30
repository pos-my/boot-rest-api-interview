package posmy.interview.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import posmy.interview.boot.enums.Roles;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("member").password("{noop}member").roles(Roles.MEMBER.name())
                .and()
                .withUser("librarian").password("{noop}librarian").roles(Roles.MEMBER.name(), Roles.LIBRARIAN.name());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/book/**").hasRole(Roles.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/api/book-record").hasRole(Roles.MEMBER.name())
                .antMatchers(HttpMethod.GET, "/api/book", "/api/member",  "/api/librarian").hasRole(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.POST, "/api/book", "/api/member", "/api/librarian").hasRole(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.PUT, "/api/book", "/api/member", "/api/librarian").hasRole(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.DELETE, "/api/book", "/api/member", "/api/librarian").hasRole(Roles.LIBRARIAN.name())
                .antMatchers(HttpMethod.PATCH, "/api/book", "/api/member", "/api/librarian").hasRole(Roles.LIBRARIAN.name())
                .antMatchers("/h2/**").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
