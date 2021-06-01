package posmy.interview.boot.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.service.UserDetailsServiceImpl;
 
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)  
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
//    	System.out.println("admin: " + passwordEncoder().encode("admin"));
//    	System.out.println("user: " + passwordEncoder().encode("user")); 	
//        auth
//        .inMemoryAuthentication()   
//            .withUser("admin").password( passwordEncoder().encode("admin") ).roles( UserRole.LIBRARIAN.name() ).and()
//            .withUser("user1").password( passwordEncoder().encode("user") ).roles( UserRole.LIBRARIAN.name() ).and()
//            .withUser("user2").password( passwordEncoder().encode("user") ).roles( UserRole.MEMBER.name() );
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	 http
    	 .headers().frameOptions().disable()
         .and()
         .csrf().disable()
         .authorizeRequests()
         .anyRequest().authenticated()
         .and()
         .formLogin().permitAll()
         .and()
         .logout().permitAll()
         .and()
         .httpBasic();
            
    }
}
