/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2021 All Rights Reserved.
 */
package posmy.interview.boot.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.entity.User;
import posmy.interview.boot.repository.UserRespository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author Bennett
 * @version $Id: AuthUserDetailServiceImpl.java, v 0.1 2021-05-24 1:50 PM Bennett Exp $$
 */
@Service
public class AuthUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRespository respository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> user = respository.findByUsername(username);

        if(user.isPresent()){
            List<GrantedAuthority> authorities = user.get().getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList());

            return buildUserDetails(username, user.get().getPassword(), authorities);
        }
        throw new UsernameNotFoundException("user not found: " + username);
    }

    private UserDetails buildUserDetails(String username, String password, List<GrantedAuthority> authorities){
         return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}