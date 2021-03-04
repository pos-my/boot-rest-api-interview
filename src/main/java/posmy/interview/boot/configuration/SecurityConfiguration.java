package posmy.interview.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import posmy.interview.boot.entity.MyUser;
import posmy.interview.boot.enums.MyRole;
import posmy.interview.boot.repos.MyUserRepository;
import posmy.interview.boot.util.Constants;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private MyUserRepository myUserRepository;

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("SELECT username, password, enabled FROM custom_users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, authority FROM custom_users WHERE username = ?");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        MyUser defaultLibrarian = MyUser.builder()
                .username(Constants.DEFAULT_LIBRARIAN_USERNAME)
                .password(passwordEncoder().encode(Constants.DEFAULT_LIBRARIAN_PASSWORD))
                .authority(MyRole.LIBRARIAN.authority)
                .enabled(true)
                .build();
        MyUser defaultMember = MyUser.builder()
                .username(Constants.DEFAULT_MEMBER_USERNAME)
                .password(passwordEncoder().encode(Constants.DEFAULT_MEMBER_PASSWORD))
                .authority(MyRole.MEMBER.authority)
                .enabled(true)
                .build();
        myUserRepository.saveAll(List.of(defaultLibrarian, defaultMember));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
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
