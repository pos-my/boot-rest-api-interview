package posmy.interview.boot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import posmy.interview.boot.enums.MyRole;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("user001")
                .password(encoder.encode("pass"))
                .roles(MyRole.LIBRARIAN.name())
                .and()
                .withUser("user002")
                .password(encoder.encode("pass"))
                .roles(MyRole.MEMBER.name());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/librarian/**")
                    .hasRole(MyRole.LIBRARIAN.name())
                .antMatchers("/member/**")
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
