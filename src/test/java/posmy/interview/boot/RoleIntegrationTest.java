package posmy.interview.boot;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import posmy.interview.boot.base.TestBase;
import posmy.interview.boot.dto.UnitTestRequest;
import posmy.interview.boot.enums.HttpMethod;
import posmy.interview.boot.enums.Role;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleIntegrationTest extends TestBase {

    @Test
    @Order(10)
    public void When_noToken_Then_returnInvalidToken() {
        //get un-auth when no token provided
        verifyInvalidToken("/role/all", HttpMethod.GET);
    }

    @Test
    @Order(20)
    public void When_getAllWithoutAccess_Then_returnAccessDenied() {
        //without access right
        verifyAccessDenied("/role/all", HttpMethod.GET, Role.MEMBER);
    }

    @Test
    @Order(30)
    public void When_getAllWithAccess_Then_returnData() {
        //with full access right
        getAllWithAccessRight(Role.ALL);
        //with librarian access right
        getAllWithAccessRight(Role.LIBRARIAN);

    }

    private void getAllWithAccessRight(Role role) {
        this.getRequest(UnitTestRequest.builder()
                .method(HttpMethod.GET)
                .url("/role/all")
                .responseFile("data/role/positive/allResponse.json")
                .token(this.getTokenByAccess(role))
                .statusResult(status().isOk())
                .customComparator(getIdStandardComparator())
                .build());
    }
}
