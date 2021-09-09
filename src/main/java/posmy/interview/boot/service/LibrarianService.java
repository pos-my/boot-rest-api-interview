package posmy.interview.boot.service;

import posmy.interview.boot.model.BaseResponse;
import posmy.interview.boot.model.librarian.AddBooksReq;
import posmy.interview.boot.model.librarian.AddMemberReq;
import posmy.interview.boot.model.librarian.ViewMemberRes;

public interface LibrarianService {
  public abstract BaseResponse addBooks(String authHeader, AddBooksReq request);
  public abstract BaseResponse updateBook(String authHeader, String isbn, String title, String author, String year, String status, String borrowedBy);
  public abstract BaseResponse removeBook(String authHeader, String isbn);
  
  public abstract BaseResponse addMember(String authHeader, AddMemberReq request);
  public abstract BaseResponse updateMember(String authHeader, String usrId, String name, String email, String role, String status);
  public abstract ViewMemberRes viewMember(String authHeader);
  public abstract BaseResponse removeMember(String authHeader, String usrId);
}
