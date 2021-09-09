package posmy.interview.boot.model.member;

import java.util.List;

import posmy.interview.boot.model.BaseResponse;
import posmy.interview.boot.model.librarian.BookBean;

public class ViewBooksRes extends BaseResponse {
  private List<BookBean> bookList;

  public ViewBooksRes() {
  }

  public List<BookBean> getBookList() {
    return bookList;
  }

  public void setBookList(List<BookBean> bookList) {
    this.bookList = bookList;
  }

  @Override
  public String toString() {
    return "ViewBooksRes [bookList=" + bookList + "]";
  }
}
