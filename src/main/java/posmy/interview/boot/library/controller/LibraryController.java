package posmy.interview.boot.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.library.bean.*;
import static posmy.interview.boot.library.constants.CommonConstants.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LibraryController {
    private static List<Book> books = new ArrayList<Book>();
    private static List<Member> members = new ArrayList<Member>();

    private boolean isMemberLogged = false;

    @GetMapping("/")
    public String returnLibraryInfo() {
        Librarian librarian = new Librarian(1, "Stephanie", 35);
        return "Welcome to Library Management System, I'm " + librarian.getName();
    }

    public String librarianMessageDisplay(Boolean isFound, String roles, String name, String keyword) {
        if (isFound) {
            if (roles.equalsIgnoreCase(ROLE_LIBRARIAN)) {
                return name + " has been " + keyword;
            } else if (roles.equalsIgnoreCase(ROLE_MEMBER)) {
                return name + " has been " + keyword;
            } else {
                return "Error, No Roles Found!";
            }
        } else {
            return "Error, No Records Found!";
        }
    }

    public String memberMessageDisplay(Boolean isFound, String bookName, String keyword) {
        if (isFound) {
            return bookName + " status now is " + keyword;
        } else {
            return "Error, No Books Found!";
        }
    }

    /* Book Management - START */
    @GetMapping("/lib/book/add/{bookName}/{author}")
    public String addBook(@PathVariable String bookName, @PathVariable String author) {
        Book newBook = new Book(bookName, author, AVAILABLE);
        books.add(newBook);
        return librarianMessageDisplay(BL_TRUE, ROLE_LIBRARIAN, bookName, ADD);
    }

    @GetMapping("/lib/book/remove/{bookName}")
    public String removeBook(@PathVariable String bookName) {
        boolean isFound = BL_FALSE;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookName().equalsIgnoreCase(bookName)) {
                books.remove(i);
                isFound = BL_TRUE;
            }
        }

        return librarianMessageDisplay(isFound, ROLE_LIBRARIAN, bookName, REMOVE);
    }

    @GetMapping("/lib/book/update/{bookName}/{newBookName}/{newAuthorName}")
    public String updateBook(@PathVariable String bookName, @PathVariable String newBookName, @PathVariable String newAuthorName) {
        boolean isFound = BL_FALSE;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookName().equalsIgnoreCase(bookName)) {
                books.get(i).setBookName(newBookName);
                books.get(i).setAuthor(newAuthorName);
                isFound = BL_TRUE;
            }
        }

        return librarianMessageDisplay(isFound, ROLE_LIBRARIAN, bookName, UPDATE);
    }

    @GetMapping("/lib/book/view")
    public List<Book> getAllBooks() {
        return books;
    }
    /* Book Management - END */

    /* Member Management - START */
    @GetMapping("/lib/member/add/{memberName}/{age}/{address}")
    public String addMember(@PathVariable String memberName, @PathVariable int age, @PathVariable String address) {
        Member newMember = new Member(memberName, age, address, BL_FALSE);
        members.add(newMember);
        return librarianMessageDisplay(BL_TRUE, ROLE_MEMBER, memberName, ADD);
    }

    @GetMapping("/lib/member/remove{memberName}")
    public String removeMember(@PathVariable String memberName) {
        boolean isFound = BL_FALSE;
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getName().equalsIgnoreCase(memberName)) {
                members.remove(i);
                isFound = BL_TRUE;
            }
        }

        return librarianMessageDisplay(isFound, ROLE_MEMBER, memberName, REMOVE);
    }

    @GetMapping("/lib/member/update/{memberName}/{newAddress}")
    public String updateMember(@PathVariable String memberName, @PathVariable String newAddress) {
        boolean isFound = BL_FALSE;
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getName().equalsIgnoreCase(memberName)) {
                members.get(i).setAddress(newAddress);
                isFound = BL_TRUE;
            }
        }

        return librarianMessageDisplay(isFound, ROLE_MEMBER, memberName, UPDATE);
    }

    @GetMapping("/lib/member/view")
    public List<Member> getAllMembers() {
        return members;
    }
    /* Member Management - End */

    /* Member Section - Start */
    @GetMapping("/member/login/{memberName}")
    public String memberLogin(@PathVariable String memberName) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getName().equalsIgnoreCase(memberName)) {
                members.get(i).setLogged(BL_TRUE);
                isMemberLogged = BL_TRUE;
            }
        }

        if (isMemberLogged) {
            return "Welcome " + memberName;
        } else {
            return "Error, No Member Found!";
        }
    }

    @GetMapping("/member/borrow/{bookName}")
    public String borrowBook(@PathVariable String bookName) {
        boolean isFound = BL_FALSE;

        if (isMemberLogged) {
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getBookName().equalsIgnoreCase(bookName)) {
                    books.get(i).setStatus(BORROWED);
                    isFound = BL_TRUE;
                }
            }

            return memberMessageDisplay(isFound, bookName, BORROWED);
        } else {
            return "Error, No Member Logged!";
        }
    }

    @GetMapping("/member/return/{bookName}")
    public String returnBook(@PathVariable String bookName) {
        if (isMemberLogged) {
            boolean isFound = BL_FALSE;
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getBookName().equalsIgnoreCase(bookName)) {
                    books.get(i).setStatus(AVAILABLE);
                    isFound = BL_TRUE;
                }
            }

            return memberMessageDisplay(isFound, bookName, AVAILABLE);
        } else {
            return "Error, No Member Logged!";
        }
    }

    @GetMapping("/member/delete")
    public String removeMember() {
        if (isMemberLogged) {
            for (int i = 0; i < members.size(); i++) {
                if (members.get(i).isLogged()) {
                    members.remove(i);
                    isMemberLogged = BL_FALSE;
                    return "Member Successfully Removed!";
                } else {
                    return "Error, No Member Logged!";
                }
            }
        } else {
            return "Error, No Member Logged!";
        }

        return null;
    }

    @GetMapping("/member/view")
    public String viewAvailBooks() {
        String message = null;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getStatus().equalsIgnoreCase(AVAILABLE)) {
                message += books.get(i).toString();
            }
        }

        return message;
    }
    /* Member Section - End */
}