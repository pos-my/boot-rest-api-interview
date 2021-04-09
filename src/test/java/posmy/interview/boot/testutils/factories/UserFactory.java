package posmy.interview.boot.testutils.factories;

import java.util.Arrays;
import posmy.interview.boot.dto.request.CreateUserDto;
import posmy.interview.boot.dto.request.UpdateUserDto;
import posmy.interview.boot.entity.User;

public final class UserFactory {

    private static UserFactory instance;

    private UserFactory() {

    }

    public static UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }

    public User constructUser() {
        return User.builder()
            .id(1L)
            .username("username")
            .password("password")
            .firstName("firstName")
            .lastName("lastName")
            .build();
    }

    public CreateUserDto constructCreateUserDto() {
        return CreateUserDto.builder()
            .username("username")
            .password("password")
            .firstName("firstName")
            .lastName("lastName")
            .roleIds(Arrays.asList(1L))
            .build();
    }

    public UpdateUserDto constructUpdateUserDto() {
        return UpdateUserDto.builder()
            .username("username")
            .password("password")
            .firstName("firstName")
            .lastName("lastName")
            .roleIds(Arrays.asList(1L))
            .build();
    }
}
