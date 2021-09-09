package posmy.interview.boot.model.librarian;

public class BookBean {
  private String isbn;
  private String title;
  private String author;
  private String year;
  private String status;
  private String borrowedBy;
  
  public BookBean() {
  }

  public BookBean(String isbn, String title, String author, String year, String status, String borrowedBy) {
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

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
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
