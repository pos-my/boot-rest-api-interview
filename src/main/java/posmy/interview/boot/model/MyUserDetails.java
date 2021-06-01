package posmy.interview.boot.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;
 
public class MyUserDetails implements UserDetails {
 
	private static final long serialVersionUID = 1L;
	
	private User user;
     
    public MyUserDetails(User user) {
        this.user = user;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {        
    	List<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority( role.getRole().name() ));
        }
//        System.out.println(authorities.toString());
        return authorities;
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUsername();
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
 
}