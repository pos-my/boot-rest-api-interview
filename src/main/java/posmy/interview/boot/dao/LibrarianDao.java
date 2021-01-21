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
import posmy.interview.boot.entity.Librarian;

/**
 *
 * @author syahirghariff
 */
@Repository
public class LibrarianDao {
    
    @Autowired
    private EntityManager em;
    
    
    public Librarian findByUserID(String userID ){
        Session session = em.unwrap(Session.class);

        Query<Librarian> query = session.createQuery("from Librarian where userId = :userId", Librarian.class);
        query.setParameter("userId", userID);

        List<Librarian> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    
    }
    
}
