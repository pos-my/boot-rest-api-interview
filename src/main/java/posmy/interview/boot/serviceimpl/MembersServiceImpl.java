/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.serviceimpl;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import posmy.interview.boot.dao.MembersDao;
import posmy.interview.boot.entity.Members;
import posmy.interview.boot.entity.User;
import posmy.interview.boot.exception.AppException;
import posmy.interview.boot.service.MembersService;
import posmy.interview.boot.service.UserService;
import posmy.interview.boot.util.Constants;

/**
 *
 * @author syahirghariff
 */
@Service
public class MembersServiceImpl implements MembersService {

    @Autowired
    private MembersDao membersDao;

    @Autowired
    private UserService userSvc;

    @Override
    public List<Members> getAllMembers() {
        return membersDao.getAllMembers();
    }

    @Override
    @Transactional
    public Members saveOrUpdate(Members req) {

        // Create new member
        if (req.getId() == null) {
            User user = userSvc.createMembers(req.getUsername());
            
            return membersDao.saveOrUpdate(Members.createMember(req, user));
        }
        
        // Update member
        return membersDao.saveOrUpdate(new Members(req));
    }

    @Override
    @Transactional
    public void delete(String id) {

        Members members = membersDao.findById(id);

        if (members == null) {
            throw new AppException(Constants.ID_NOT_FOUND.concat(id));
        }
        
        
        membersDao.deleteById(id);
        userSvc.deleteMembers(members.getUserId());

    }

    @Override
    public Members findFromLogin() {
        User user = userSvc.findByUsername();
        return membersDao.findByUserID(user.getId());
    }
    
    

}
