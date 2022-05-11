package posmy.interview.boot.library.common;

import static posmy.interview.boot.library.constants.CommonConstants.*;

public class CommonHelper {
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
}
