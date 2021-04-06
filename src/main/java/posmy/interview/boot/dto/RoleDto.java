package posmy.interview.boot.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto extends BaseDto {
    private String id;
    private String name;
    private List<UserDto> users;
}
