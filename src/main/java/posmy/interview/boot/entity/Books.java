/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posmy.interview.boot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import posmy.interview.boot.enums.BookStatus;

/**
 *
 * @author syahirghariff
 */
@Entity
@Table(name="BOOKS")
public class Books implements Serializable {
    
    
    @Id
    @Column(name = "ID")
    private String id; 
    
    @Column(name = "BOOK_NAME")
    @NotNull
    private String bookName; 
    
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BookStatus status; 
    
    @Column(name="BORROWED_BY")
    private String borrowedBy;
    
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date createdDate; 
    
    @Column(name="CREATED_BY")
    private String createdBy;
    
    @ManyToOne
    @JoinColumn(name="CREATED_BY", insertable = false, updatable = false)
    private Librarian librarian;
    
    @ManyToOne
    @JoinColumn(name="BORROWED_BY", insertable = false, updatable = false)
    private Members members; 

    public Books() {
    }
    
    public Books(Books req, Librarian librarian){
        
        this.id = req.getId() != null ? req.getId() : UUID.randomUUID().toString(); 
        this.bookName = req.getBookName().trim();
        this.status = req.getStatus() != null ? req.getStatus() : BookStatus.AVAILABLE; 
        this.borrowedBy = req.getBorrowedBy() != null ? req.getBorrowedBy() : null;
        this.createdDate = req.getCreatedDate() != null ? req.getCreatedDate() : new Date(); 
        this.createdBy = req.getCreatedBy() != null ? req.getCreatedBy() : librarian.getId();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(String borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Librarian getLibrarian() {
        return librarian;
    }

    public void setLibrarian(Librarian librarian) {
        this.librarian = librarian;
    }

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Books{" + "id=" + id + ", bookName=" + bookName + ", status=" + status + ", borrowedBy=" + borrowedBy + ", createdDate=" + createdDate + ", createdBy=" + createdBy + ", librarian=" + librarian + ", members=" + members + '}';
    }
    
    

    
}
