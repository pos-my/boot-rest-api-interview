package posmy.interview.boot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import posmy.interview.boot.enums.MyRole;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        UserDetails defaultLibrarian = User.withUsername("user001")
                .passwordEncoder(s -> passwordEncoder().encode(s))
                .password("pass")
                .roles(MyRole.LIBRARIAN.name())
                .build();
        manager.createUser(defaultLibrarian);
        UserDetails defaultMember = User.withUsername("user002")
                .passwordEncoder(s -> passwordEncoder().encode(s))
                .password("pass")
                .roles(MyRole.MEMBER.name())
                .build();
        manager.createUser(defaultMember);
        return manager;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v1/librarian/**")
                    .hasRole(MyRole.LIBRARIAN.name())
                .antMatchers("/v1/member/**")
                    .hasRole(MyRole.MEMBER.name())
                .antMatchers("/h2-console", "/console/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                    .httpBasic()
                    .authenticationEntryPoint(basicAuthenticationEntryPoint());
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Bean
    public AuthenticationEntryPoint basicAuthenticationEntryPoint() {
        BasicAuthenticationEntryPoint authenticationEntryPoint = new BasicAuthenticationEntryPoint();
        authenticationEntryPoint.setRealmName("posmy");
        return authenticationEntryPoint;
    }
}
