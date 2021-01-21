/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.service;

import java.util.List;
import posmy.interview.boot.entity.Books;

/**
 *
 * @author syahirghariff
 */
public interface BooksService {
    
    public List<Books> getAllBooks();
    
    public Books saveOrUpdate(Books req);
    
    public Books borrowBooks(String bookName); 
    
    public Books returnBooks(String bookName);
    
    public void delete(String id);
    
    
    
}
