/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.service;

import org.springframework.security.core.userdetails.UserDetails;
import posmy.interview.boot.entity.User;

/**
 *
 * @author syahirghariff
 */
public interface UserService {
    
    public UserDetails findByUsername(String username);
    
    public User findByUsername();
    
    public User createMembers(String username);
    
    public void deleteMembers(String id);
    
}
