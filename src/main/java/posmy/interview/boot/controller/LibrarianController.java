package posmy.interview.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import posmy.interview.boot.enums.ResponseStatus;
import posmy.interview.boot.model.BaseResponse;
import posmy.interview.boot.model.librarian.AddBooksReq;
import posmy.interview.boot.model.librarian.AddMemberReq;
import posmy.interview.boot.model.librarian.ViewMemberRes;
import posmy.interview.boot.service.LibrarianService;

@RestController
@Api(value = "Librarian", tags = { "Librarian" })
public class LibrarianController {
  private static final Logger logger = LoggerFactory.getLogger(LibrarianController.class);

  @Autowired
  LibrarianService librarianService;

  @PostMapping("/librarian/books")
  @ApiOperation(value = "Add new books")
  public ResponseEntity<BaseResponse> addNewBooks(@RequestHeader("Authorization") String authHeader,
      @RequestBody AddBooksReq request) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/librarian/books] API called: {}", request.toString());
    try {
      response = librarianService.addBooks(authHeader, request);
      if (response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      logger.error("POST [/librarian/books] Exception found", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/librarian/books/{isbn}")
  @ApiOperation(value = "Update book")
  public ResponseEntity<BaseResponse> updateBook(@RequestHeader("Authorization") String authHeader,
      @PathVariable("isbn") String isbn, @RequestParam("title") String title, @RequestParam("author") String author,
      @RequestParam("year") String year, @RequestParam("status") String status, @RequestParam("borrowedBy") String borrowedBy) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/librarian/books/{}] API called", isbn);
    try {
      response = librarianService.updateBook(authHeader, isbn, title, author, year, status, borrowedBy);
      if (response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      logger.error("POST [/librarian/books/{}] Exception found", isbn, e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/librarian/books/{isbn}/remove")
  @ApiOperation(value = "Remove book")
  public ResponseEntity<BaseResponse> removeBook(@RequestHeader("Authorization") String authHeader,
      @PathVariable("isbn") String isbn) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/librarian/books/{}/remove] API called", isbn);
    try {
      response = librarianService.removeBook(authHeader, isbn);
      if (response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      logger.error("POST [/librarian/books/{}/remove] Exception found", isbn, e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/librarian/member")
  @ApiOperation(value = "Add member")
  public ResponseEntity<BaseResponse> addMember(@RequestHeader("Authorization") String authHeader,
      @RequestBody AddMemberReq request) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/librarian/member] API called: {}", request.toString());
    try {
      response = librarianService.addMember(authHeader, request);
      if (response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      logger.error("POST [/librarian/member] Exception found", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/librarian/member/{usrId}")
  @ApiOperation(value = "Update member")
  public ResponseEntity<BaseResponse> updateMember(@RequestHeader("Authorization") String authHeader,
      @PathVariable("usrId") String usrId, @RequestParam("name") String name, @RequestParam("email") String email,
      @RequestParam("role") String role, @RequestParam("status") String status

  ) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/librarian/member/{}] API called", usrId);
    try {
      response = librarianService.updateMember(authHeader, usrId, name, email, role, status);
      if (response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      logger.error("POST [/librarian/member/{}] Exception found", usrId, e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/librarian/member")
  @ApiOperation(value = "View member")
  public ResponseEntity<ViewMemberRes> viewMember(@RequestHeader("Authorization") String authHeader) {

    ViewMemberRes response = new ViewMemberRes();
    logger.info("GET [/librarian/member] API called");
    try {
      response = librarianService.viewMember(authHeader);
      if (response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      logger.error("GET [/librarian/member] Exception found", e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @PostMapping("/librarian/member/{usrId}/remove")
  @ApiOperation(value = "Remove member")
  public ResponseEntity<BaseResponse> removeMember(
    @RequestHeader("Authorization") String authHeader,
    @PathVariable("usrId") String usrId
  ) {

    BaseResponse response = new BaseResponse();
    logger.info("POST [/librarian/member/{}/remove] API called", usrId);
    try {
      response = librarianService.removeMember(authHeader, usrId);
      if (response.getStatus().equals(ResponseStatus.SUCCESS.getCode())) {
        return new ResponseEntity<>(response, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
      }
    } catch (Exception e) {
      logger.error("POST [/librarian/member/{}/remove] Exception found", usrId, e);
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
