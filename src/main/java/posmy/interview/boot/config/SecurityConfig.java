package posmy.interview.boot.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import posmy.interview.boot.entities.RoleRepository;
import posmy.interview.boot.entities.UserRepository;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

//	@Override
//	protected UserDetailsService userDetailsService() {
//		
//		List<UserDetails> userDetails = new ArrayList<>();
//		userDetails.add(User.withUsername("librarian").password("librarian").roles("ADMIN", "USER").build());
//		userDetails.add(User.withUsername("member").password("member").roles("USER").build());
//		return new InMemoryUserDetailsManager(userDetails);
//	}
//
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
        		.antMatchers("/*").permitAll()
        		.antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/member/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/librarian/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().formLogin();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
