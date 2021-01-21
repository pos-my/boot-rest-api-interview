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
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import org.springframework.stereotype.Repository;
import posmy.interview.boot.entity.Books;
import posmy.interview.boot.enums.BookStatus;

/**
 *
 * @author syahirghariff
 */
@Repository
public class BooksDao {
    
    @Autowired
    private EntityManager em;
    
    
    public List<Books> getAllBooks() {

        Session session = em.unwrap(Session.class);

        Query<Books> query = session.createQuery("from Books", Books.class);

        return query.getResultList();
    }
    
    
    public Books saveOrUpdate(Books books) {
        Session session = em.unwrap(Session.class);
        session.saveOrUpdate(books);
        
        return books;
    }
    
    
    public void deleteById(String id) {
        Session currentSession = em.unwrap(Session.class); 
        
        Query query = currentSession.createQuery("delete from Books where id=:id"); 
        query.setParameter("id", id);
        
        query.executeUpdate();
    }
    
    public Books findById(String id ){
        Session session = em.unwrap(Session.class);

        Query<Books> query = session.createQuery("from Books where id = :id", Books.class);
        query.setParameter("id", id);

        List<Books> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    
    }
    
    public List<Books> findAllByBookName(String bookName) {
    
        Session session = em.unwrap(Session.class);

        Query<Books> query = session.createQuery("from Books where bookName = :bookName", Books.class);
        query.setParameter("bookName", bookName);

        List<Books> result = query.getResultList();

        return result;
    }
    
    public Books findBookByBookNameAndMembers(String bookName, String memberId) {
        
        Session session = em.unwrap(Session.class);

        Query<Books> query = session.createQuery("from Books where borrowedBy = :memberId and bookName = :bookName and status = :status", Books.class);
        query.setParameter("memberId", memberId);
        query.setParameter("bookName", bookName);
        query.setParameter("status", BookStatus.BORROWED);

        List<Books> result = query.getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }

        return null;
    
    }
    
}
