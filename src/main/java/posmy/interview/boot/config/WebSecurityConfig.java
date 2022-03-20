package posmy.interview.boot.config;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import posmy.interview.boot.controllers.filters.AuthTokenFilter;
import posmy.interview.boot.domains.UserRoles;
import posmy.interview.boot.services.UserDetailsServiceImpl;
import posmy.interview.boot.utils.AuthEntryPointJwt;

@Configuration
@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final String [] SWAGGER_WHITELIST = {"/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"};
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authEntryPointJwt)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeHttpRequests().antMatchers("/**").permitAll()
                //.and().authorizeHttpRequests().antMatchers(SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated().and().httpBasic();
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().disable();
    }

    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    //    http.cors()
   //             .and().csrf().disable()
                //.exceptionHandling().authenticationEntryPoint(authEntryPointJwt)
   //             .authorizeHttpRequests().antMatchers(SWAGGER_WHITELIST).permitAll().and()
   //             .authorizeRequests()
   //             .antMatchers("/swagger-ui*/**", "/assessment-openapi/**").permitAll()
    //            .anyRequest().authenticated().and().httpBasic();
    //    http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    //}

}
