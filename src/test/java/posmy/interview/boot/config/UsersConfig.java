package posmy.interview.boot.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import posmy.interview.boot.domains.Users;
import posmy.interview.boot.services.UserDetailsImpl;

import java.util.Arrays;

@TestConfiguration
public class UsersConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        User.UserBuilder usrBuilder = User.builder();
        UserDetails librarian = usrBuilder.username("lib")
                .password(encoder().encode("d")).roles("LIBRARIAN").authorities("LIBRARIAN").build();

        UserDetails member = usrBuilder.username("mem")
                .password(encoder().encode("d")).roles("MEMBER").authorities("MEMBER").build();


        return new InMemoryUserDetailsManager(Arrays.asList(
                librarian, member
        ));
    }
}