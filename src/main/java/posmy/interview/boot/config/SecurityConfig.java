package posmy.interview.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.headers().frameOptions().sameOrigin()
		.and()
		.csrf().disable()
		.httpBasic()
		.and()
		.authorizeRequests()
		.antMatchers("/member/**").hasRole("MEMBER")
		.antMatchers("/library/**").hasRole("LIBRARIAN")
		.antMatchers("/").denyAll()
		.antMatchers("/h2-console/**").permitAll()
		.and()
		.formLogin();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("Ben").password("{noop}Today@2022").roles("MEMBER")
		.and()
		.withUser("John").password("{noop}Today@2022").roles("LIBRARIAN");
	}

}