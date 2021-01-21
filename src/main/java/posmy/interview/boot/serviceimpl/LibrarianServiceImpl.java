/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dao.LibrarianDao;
import posmy.interview.boot.entity.Librarian;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.service.LibrarianService;
import posmy.interview.boot.service.UserService;

/**
 *
 * @author syahirghariff
 */

@Service
public class LibrarianServiceImpl implements LibrarianService {
    
    @Autowired
    private LibrarianDao librarianDao;
    
    @Autowired
    private UserService userSvc;

    @Override
    public Librarian findFromLogin() {
        
        User user = userSvc.findByUsername();
        
        return librarianDao.findByUserID(user.getId());
    }
    
    
    
    
}
