package posmy.interview.boot.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import posmy.interview.boot.service.impl.UserServiceImpl;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	  @Bean
	  public UserDetailsService userDetailsService() {
	    return new UserServiceImpl();
	  };
	  
	  @Bean
	  public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	  };
    
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .httpBasic()
        .and()
        .authorizeRequests()//.anyRequest().authenticated()
        .antMatchers("/").permitAll()
        .antMatchers("/lib/**").hasRole("LIBRARIAN")
        .antMatchers("/mem/**").hasRole("MEMBER")
        .and()
        .csrf().disable()
        .formLogin().disable();
    }

}