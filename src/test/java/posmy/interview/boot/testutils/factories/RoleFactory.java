package posmy.interview.boot.testutils.factories;

import java.util.ArrayList;
import java.util.List;
import posmy.interview.boot.entity.Role;

public final class RoleFactory {

    private static RoleFactory instance;

    private RoleFactory() {

    }

    public static RoleFactory getInstance() {
        if (instance == null) {
            instance = new RoleFactory();
        }
        return instance;
    }

    public Role constructRole() {
        return Role.builder()
            .id(1L)
            .name("ROLE_LIBRARIAN")
            .description("librarian")
            .build();
    }

    public List<Role> constructListOfRole() {
        List<Role> roles = new ArrayList<>();
        roles.add(constructRole());
        return roles;
    }
}
