package posmy.interview.boot.dao;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "BOOK", schema = "C##TEST")
public class Book implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "ISBN")
  private String isbn;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "AUTHOR")
  private String author;

  @Column(name = "YEAR")
  private String year;
  
  @Column(name = "STATUS")
  private String status;
  
  @Column(name = "BORROWED_BY")
  private String borrowedBy;

  public Book() {
  }

  public Book(String isbn, String title, String author, String year, String status, String borrowedBy) {
    this.isbn = isbn;
    this.title = title;
    this.author = author;
    this.year = year;
    this.status = status;
    this.borrowedBy = borrowedBy;
  }  

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getBorrowedBy() {
    return borrowedBy;
  }

  public void setBorrowedBy(String borrowedBy) {
    this.borrowedBy = borrowedBy;
  }

  @Override
  public String toString() {
    return "Book [author=" + author + ", isbn=" + isbn + ", status=" + status + ", title=" + title + ", year=" + year 
        + ", borrowedBy=" + borrowedBy + "]";
  }

}