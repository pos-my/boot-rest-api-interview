package posmy.interview.boot.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SystemUser implements UserDetails{
	
	private PasswordEncoder passwordEncoder;
	
	private final List<? extends GrantedAuthority> grantedAuthorities;
	private final String password;
	private final String username;
	private final boolean isAccountNonExpired;
	private final boolean isAccountNonLocked;
	private final boolean isCredentialsNonExpired;
	private final boolean isEnabled;
	
	public SystemUser(UserDetail user) {
		
		passwordEncoder = new BCryptPasswordEncoder(10);
		
		this.username = user.getUsername();
		this.password = passwordEncoder.encode(user.getPassword());
		this.grantedAuthorities = Arrays.stream(user.getRoles().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		this.isAccountNonExpired =  isAccountNonExpired();
		this.isAccountNonLocked = isAccountNonLocked();
		this.isCredentialsNonExpired = isCredentialsNonExpired();
		this.isEnabled = isEnabled();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
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

}
