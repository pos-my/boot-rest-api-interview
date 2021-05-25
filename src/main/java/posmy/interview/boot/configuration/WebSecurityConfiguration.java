/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Bennett
 * @version $Id: WebSecurityConfiguration.java, v 0.1 2021-05-24 12:43 PM Bennett Exp $$
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordConfiguration passwordConfiguration;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeRequests()
//            .antMatchers("/api/**").hasRole(UserRole.LIBRARIAN.name())
//            .antMatchers(HttpMethod.POST, "/api/**").hasAnyAuthority(ApplicationAuthorityPermission.CREATE_MEMBER.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails bennettUser = User.builder()
//                .username("bennett")
//                .password(passwordEncoder.encode("123"))
//                .roles(RolePermissionEnum.LIBRARIAN.name())
//                .build();
//
//        UserDetails secondUser = User.builder()
//                .username("member")
//                .password(passwordEncoder.encode("123"))
//                .roles(RolePermissionEnum.MEMBER.name())
//                .build();
//
//        return new InMemoryUserDetailsManager(
//                bennettUser,
//                secondUser);
//    }
}
