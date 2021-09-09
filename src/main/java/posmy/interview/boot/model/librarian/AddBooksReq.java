package posmy.interview.boot.model.librarian;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class AddBooksReq {
  private List<BookBean> books;

  public List<BookBean> getBooks() {
    return books;
  }

  public void setBooks(List<BookBean> books) {
    this.books = books;
  }

  @Override
  public String toString() {
    return "AddBooksReq [books=[" + StringUtils.join(books.toString()) + "]]";
  }
}
