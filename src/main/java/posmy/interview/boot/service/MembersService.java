/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.service;

import java.util.List;
import posmy.interview.boot.entity.Members;

/**
 *
 * @author syahirghariff
 */
public interface MembersService {
    
    public List<Members> getAllMembers();
    
    public Members saveOrUpdate(Members req); 
    
    public void delete(String id);
    
    public Members findFromLogin();
    
    
    
}
