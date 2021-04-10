package posmy.interview.boot.mapper;

import java.util.List;
import org.springframework.util.ObjectUtils;
import posmy.interview.boot.dto.request.CreateUserDto;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.entity.User;

public class UserMapper {

    private static UserMapper userMapperInstance;

    public static UserMapper getInstance() {
        if (ObjectUtils.isEmpty(userMapperInstance)) {
            userMapperInstance = new UserMapper();
        }

        return userMapperInstance;
    }

    public User mapCreateUserDtoToEntity(
        CreateUserDto createUserDto,
        String encodedPassword,
        List<Role> roles
    ) {
        return User.builder()
            .username(createUserDto.getUsername())
            .password(encodedPassword)
            .firstName(createUserDto.getFirstName())
            .lastName(createUserDto.getLastName())
            .roles(roles)
            .build();
    }
}
