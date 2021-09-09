package posmy.interview.boot.service;

import java.util.List;

import posmy.interview.boot.model.BaseResponse;
import posmy.interview.boot.model.member.ViewBooksRes;

public interface MemberService {
  public abstract ViewBooksRes viewBooks(String authHeader);
  public abstract BaseResponse borrowBooks(String authHeader, List<String> isbnList);
  public abstract BaseResponse returnBooks(String authHeader, List<String> isbnList);
  
  public abstract BaseResponse deleteAccount(String authHeader);
   
}
