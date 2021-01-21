/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Members;

/**
 *
 * @author syahirghariff
 */
@Repository 
public class MembersDao {
    
    @Autowired
    private EntityManager em;
    
    
    public List<Members> getAllMembers() {

        Session session = em.unwrap(Session.class);

        Query<Members> query = session.createQuery("from Members", Members.class);

        return query.getResultList();
    }
    
    public Members saveOrUpdate(Members req) {
    
        Session session = em.unwrap(Session.class);
        session.saveOrUpdate(req);
        
        return req;
    }
    
    public void deleteById(String id) {
        Session currentSession = em.unwrap(Session.class); 
        
        Query query = currentSession.createQuery("delete from Members where id=:id"); 
        query.setParameter("id", id);
        
        query.executeUpdate();
    }
    
    public Members findById(String id ){
        Session session = em.unwrap(Session.class);

        Query<Members> query = session.createQuery("from Members where id = :id", Members.class);
        query.setParameter("id", id);

        List<Members> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    
    }
    
    
    public Members findByUserID(String userID) {
        
         Session session = em.unwrap(Session.class);

        Query<Members> query = session.createQuery("from Members where userId = :userId", Members.class);
        query.setParameter("userId", userID);

        List<Members> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }
    
}
