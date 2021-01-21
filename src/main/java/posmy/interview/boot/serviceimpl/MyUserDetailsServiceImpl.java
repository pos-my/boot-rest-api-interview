/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.service.UserService;

/**
 *
 * @author syahirghariff
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    private UserService userSvc;
    
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userSvc.findByUsername(userName);
    }
    
}
