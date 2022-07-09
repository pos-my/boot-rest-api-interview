package posmy.interview.boot.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import posmy.interview.boot.constant.Roles;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable()
			.authorizeHttpRequests()
			.antMatchers(HttpMethod.GET, "/member/**").hasRole(Roles.LIBRARIAN.name())
			.antMatchers(HttpMethod.POST, "/member/**").hasRole(Roles.LIBRARIAN.name())
			.antMatchers(HttpMethod.DELETE, "/member/**").hasAnyRole(Roles.LIBRARIAN.name(), Roles.MEMBER.name())
			.antMatchers(HttpMethod.GET,"/book/**").hasAnyRole(Roles.LIBRARIAN.name(), Roles.MEMBER.name())
			.antMatchers(HttpMethod.POST, "/book/**").hasRole(Roles.LIBRARIAN.name())
			.antMatchers(HttpMethod.DELETE, "/book/**").hasAnyRole(Roles.LIBRARIAN.name())
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
		
		return http.build();
		
     
    }
    
}
