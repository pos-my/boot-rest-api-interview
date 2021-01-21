/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.enums.ActiveStatus;

/**
 *
 * @author syahirghariff
 */
public class MyUserDetails implements UserDetails{
    
    private String userName;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    
    public MyUserDetails(User user) {
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.active = user.getActive().toString().equals(ActiveStatus.A.toString());
        this.authorities = Arrays.stream(user.getRole().toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

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
        return userName;
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
        return active;
    }

    @Override
    public String toString() {
        return "MyUserDetails{" + "userName=" + userName + ", password=" + password + ", active=" + active + ", authorities=" + authorities + '}';
    }
    
    
}
