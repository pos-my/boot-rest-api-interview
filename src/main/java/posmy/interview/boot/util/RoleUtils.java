package posmy.interview.boot.util;

import posmy.interview.boot.entity.UsersEntity;
import posmy.interview.boot.enums.Roles;

public class RoleUtils {

    private RoleUtils() {}

    public static boolean isNotMember(UsersEntity usersEntity) {
        return usersEntity.getRoles().size() != 1 || usersEntity.getRoles().stream().noneMatch(role -> role.getName().equalsIgnoreCase(Roles.MEMBER.name()));
    }
}
