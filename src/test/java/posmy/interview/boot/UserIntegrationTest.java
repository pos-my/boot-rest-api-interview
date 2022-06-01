package posmy.interview.boot;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import posmy.interview.boot.base.TestBase;
import posmy.interview.boot.dto.UnitTestRequest;
import posmy.interview.boot.enums.HttpMethod;
import posmy.interview.boot.enums.Role;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest extends TestBase {


    @Test
    @Order(10)
    public void When_noToken_Then_returnInvalidToken() {
        //get un-auth when no token provided
        verifyInvalidToken("/user", HttpMethod.POST);
        verifyInvalidToken("/user/name/ck", HttpMethod.GET);
        verifyInvalidToken("/user/me", HttpMethod.DELETE);
        verifyInvalidToken("/user/name/ck", HttpMethod.DELETE);
    }

    @Test
    @Order(20)
    public void When_getAllWithoutAccess_Then_returnAccessDenied() {
        //without access right
        verifyAccessDenied("/user", HttpMethod.POST, Role.MEMBER);
        verifyAccessDenied("/user/name/ck", HttpMethod.GET, Role.MEMBER);
        verifyAccessDenied("/user/me", HttpMethod.DELETE, Role.LIBRARIAN);
        verifyAccessDenied("/user/name/ck", HttpMethod.DELETE, Role.MEMBER);
    }

    @Test
    @Order(30)
    public void When_saveWithAccess_Then_saveAndReturnData() {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.POST)
                .url("/user/")
                .requestFile("data/user/positive/save.json")
                .responseFile("data/user/positive/saveResponse.json")
                .token(this.getTokenByAccess(Role.LIBRARIAN))
                .statusResult(status().isOk())
                .customComparator(getIdStandardComparator())
                .build());
    }

    @Test
    @Order(40)
    public void When_getUserNameWithAccess_Then_returnData() {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/user/name/ck")
                .responseFile("data/user/positive/nameResponse.json")
                .token(this.getTokenByAccess(Role.LIBRARIAN))
                .statusResult(status().isOk())
                .customComparator(getIdStandardComparator())
                .build());
    }

    @Test
    @Order(50)
    public void When_deleteMeWithAccess_Then_deleteData() {
        //todo: in here we should implement run script to insert pre-load data in future
        //get test2 member user token
        var token = generateToken("test2", List.of("MEMBER"));
        //delete test1 user
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.DELETE)
                .url("/user/me")
                .token(token)
                .statusResult(status().isOk())
                .build());
        //user deleted + verify user not exists UT
        verifyUserNotExists("test2");
    }

    @Test
    @Order(60)
    public void When_deleteUserWithAccess_Then_deleteData() {

        //delete test1 user
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.DELETE)
                .url("/user/name/test1")
                .token(this.getTokenByAccess(Role.LIBRARIAN))
                .statusResult(status().isOk())
                .build());
        //user deleted + verify user not exists UT
        verifyUserNotExists("test1");
    }


    private void verifyUserNotExists(String username) {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/user/name/" + username)
                .responseFile("data/user/negative/notExistsResponse.json")
                .token(this.getTokenByAccess(Role.LIBRARIAN))
                .statusResult(status().isBadRequest())
                .customComparator(getIdStandardComparator())
                .build());
    }


}
