package posmy.interview.boot.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic().disable();
//
//        http.authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/h2-ui/**").permitAll();
//
//        http.csrf().disable();
//
//        http.headers().frameOptions().disable();
    }

}
