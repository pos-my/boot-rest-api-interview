/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.serviceimpl;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dao.UserDao;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.exception.AppException;
import posmy.interview.boot.model.MyUserDetails;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.util.Constants;

/**
 *
 * @author syahirghariff
 */
@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails findByUsername(String username) {
        
        User user = userDao.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("Not found: ".concat(username));
        }
        
        return new MyUserDetails(user);
    }

    @Override
    public User findByUsername() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userDao.findByUsername(auth.getName());
    }

    @Override
    @Transactional
    public User createMembers(String username) {
       User user = User.createMembers(username);
       return userDao.saveOrUpdate(user);
    }

    @Override
    public void deleteMembers(String id) {
         User user = userDao.findById(id);

        if (user == null) {
            throw new AppException(Constants.ID_NOT_FOUND.concat(id));
        }

        userDao.deleteById(id);
    }
    
    
    
    
    
    
    
    
    
    
}
