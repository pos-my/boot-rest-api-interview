/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.dao;

import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.User;

/**
 *
 * @author syahirghariff
 */
@Repository 
public class UserDao {
    
    
    @Autowired
    private EntityManager em;
    
    public User findByUsername(String username) {
        
        Session session = em.unwrap(Session.class);

        Query<User> query = session.createQuery("from User where username = :username", User.class);
        query.setParameter("username", username);

        List<User> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    
    }
    
    public User saveOrUpdate(User user) {
        Session session = em.unwrap(Session.class);
        session.saveOrUpdate(user);
        
        return user;
    }
    
    public User findById(String id) {
        
        Session session = em.unwrap(Session.class);

        Query<User> query = session.createQuery("from User where id = :id", User.class);
        query.setParameter("id", id);

        List<User> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    
    }
    
    public void deleteById(String id) {
        Session currentSession = em.unwrap(Session.class); 
        
        Query query = currentSession.createQuery("delete from User where id=:id"); 
        query.setParameter("id", id);
        
        query.executeUpdate();
    }
    
    
}
