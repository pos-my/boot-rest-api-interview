package posmy.interview.boot.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import posmy.interview.boot.enums.ResponseStatus;
import posmy.interview.boot.model.BaseResponse;
import posmy.interview.boot.model.member.ViewBooksRes;
import posmy.interview.boot.service.MemberService;

@RestController
@Api(value = "Member", tags = { "Member" })
public class MemberController {
  private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
  
  @Autowired
  MemberService memberService;
  
  @GetMapping("/member/books")
  @ApiOperation(value = "View books")
  public ResponseEntity<ViewBooksRes> viewBooks(@RequestHeader("Authorization") String authHeader) {

    ViewBooksRes response = new ViewBooksRes();
    logger.info("GET [/member/books] API called");
    try {
      response = memberService.viewBooks(authHeader);
      if(response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);      
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);   
      }
    } catch (Exception e) {
      logger.error("GET [/member/books] Exception found", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @PostMapping("/member/books/borrow")
  @ApiOperation(value = "Borrow books")
  public ResponseEntity<BaseResponse> borrowBooks(@RequestHeader("Authorization") String authHeader,
      @RequestParam("isbn") List<String> isbnList) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/member/books/borrow] API called: {}", StringUtils.join(isbnList));
    try {
      response = memberService.borrowBooks(authHeader, isbnList);
      if(response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);      
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);   
      }
    } catch (Exception e) {
      logger.error("POST [/member/books/borrow] Exception found", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @PostMapping("/member/books/return")
  @ApiOperation(value = "Return books")
  public ResponseEntity<BaseResponse> returnBooks(@RequestHeader("Authorization") String authHeader,
      @RequestParam("isbn") List<String> isbnList) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/member/books/return] API called: {}", StringUtils.join(isbnList));
    try {
      response = memberService.returnBooks(authHeader, isbnList);
      if(response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);      
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);   
      }
    } catch (Exception e) {
      logger.error("POST [/member/books/return] Exception found", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @PostMapping("/member/remove")
  @ApiOperation(value = "Delete account")
  public ResponseEntity<BaseResponse> deleteAccount(@RequestHeader("Authorization") String authHeader) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/member/remove] API called: {}", authHeader);
    try {
      response = memberService.deleteAccount(authHeader);
      if(response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);      
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);   
      }
    } catch (Exception e) {
      logger.error("POST [/member/remove] Exception found", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
