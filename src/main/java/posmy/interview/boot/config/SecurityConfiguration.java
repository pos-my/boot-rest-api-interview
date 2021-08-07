package posmy.interview.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import lombok.experimental.FieldDefaults;
import posmy.interview.boot.security.NoRedirectStrategy;
import posmy.interview.boot.security.TokenAuthenticationProvider;
import posmy.interview.boot.security.TokenAuthenticationFilter;

import posmy.interview.boot.service.impl.UserDetailsServiceImpl;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	 private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
	    new AntPathRequestMatcher("/login/**"),
	    new AntPathRequestMatcher("/error/**"),
	    new AntPathRequestMatcher("/h2-console/**")
	  );
	  private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
	
	  TokenAuthenticationProvider provider;
	
	  SecurityConfiguration(TokenAuthenticationProvider provider) {
	    super();
	    this.provider = requireNonNull(provider);
	  }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new UserDetailsServiceImpl();
//    }

 
	
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService());
//        //authProvider.setPasswordEncoder(passwordEncoder());
//         
//        return authProvider;
//    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(provider);
        
//        auth.inMemoryAuthentication()
//        .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("LIBRARIAN")
//        .and()
//        .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("MEMBER");
    }
    
    @Override
    public void configure(final WebSecurity web) {
      web.ignoring().requestMatchers(PUBLIC_URLS);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
////            .antMatchers("/").hasAnyAuthority("LIBRARIAN", "MEMBER")
//            .antMatchers("/user/").hasAnyAuthority("LIBRARIAN")
//            .antMatchers("/book/**").hasAnyAuthority("LIBRARIAN")
//            .antMatchers("/book/all").hasAuthority("MEMBER")
//            .antMatchers("/book/get").hasAuthority("MEMBER")
//            .antMatchers("/book/borrow").hasAuthority("MEMBER")
//            .antMatchers("/book/return").hasAuthority("MEMBER")
//            .anyRequest().authenticated()
//            .and()
//            .formLogin().permitAll()
//            .and()
//            .logout().permitAll()
//            .and()
//            .exceptionHandling().accessDeniedPage("/403");
//    	   http.cors().and().csrf().disable()
//    	      .sessionManagement()
//    	      .sessionCreationPolicy(STATELESS)
//    	      .and()
//    	      .exceptionHandling()
//    	      // this entry point handles when you request a protected page and you are not yet
//    	      // authenticated
//    	      //.defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
//    	      .and()
//    	      .authenticationProvider(provider)
//    	      .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
//    	      .authorizeRequests()
//          //.antMatchers("/").hasAnyAuthority("LIBRARIAN", "MEMBER")
//          .antMatchers("/user/").hasAnyAuthority("LIBRARIAN")
//          .antMatchers("/book/create").hasAuthority("LIBRARIAN")
//          .antMatchers("/book/delete").hasAuthority("LIBRARIAN")
//          .antMatchers("/book/all").hasAnyAuthority("MEMBER","LIBRARIAN")
//          .antMatchers("/book/get").hasAuthority("MEMBER")
//          .antMatchers("/book/borrow").hasAuthority("MEMBER")
//          .antMatchers("/book/return").hasAuthority("MEMBER")
//          .and()
//          .httpBasic()
//          .and()
//          .authorizeRequests()
//          .anyRequest()
//    	      .authenticated()
//    	      .and()
//    	      .formLogin().disable()
//    	      //.httpBasic().disable()
//    	      .logout().disable();    	
    	  http
          .sessionManagement()
          .sessionCreationPolicy(STATELESS)
          .and()
          .exceptionHandling()
          // this entry point handles when you request a protected page and you are not yet
          // authenticated
          .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
          .and()
          .authenticationProvider(provider)
          .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
          .authorizeRequests()
        .antMatchers("/user/create").hasAuthority("LIBRARIAN")
        .antMatchers("/user/update").hasAuthority("LIBRARIAN")
        .antMatchers("/user/all").hasAuthority("LIBRARIAN")
        .antMatchers("/user/get/**").hasAuthority("LIBRARIAN")
        .antMatchers("/user/delete").hasAnyAuthority("LIBRARIAN","MEMBER")
        .antMatchers("/book/create").hasAuthority("LIBRARIAN")
        .antMatchers("/book/delete").hasAuthority("LIBRARIAN")
        .antMatchers("/book/all").hasAuthority("MEMBER")
        .antMatchers("/book/get").hasAuthority("MEMBER")
        .antMatchers("/book/borrow").hasAuthority("MEMBER")
        .antMatchers("/book/return").hasAuthority("MEMBER")
          .requestMatchers(PROTECTED_URLS)
          .authenticated()
          .and()
          .csrf().disable()
          .formLogin().disable()
          .httpBasic().disable()
          .logout().disable();
    }

    @Bean
    TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
      final TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
      filter.setAuthenticationManager(authenticationManager());
      filter.setAuthenticationSuccessHandler(successHandler());
      return filter;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
      final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
      successHandler.setRedirectStrategy(new NoRedirectStrategy());
      return successHandler;
    }

    /**
     * Disable Spring boot automatic filter registration.
     */
    @Bean
    FilterRegistrationBean disableAutoRegistration(final TokenAuthenticationFilter filter) {
      final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
      registration.setEnabled(false);
      return registration;
    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
      return new HttpStatusEntryPoint(FORBIDDEN);
    }
    

}
