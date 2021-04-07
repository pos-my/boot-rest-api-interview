package posmy.interview.boot.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto extends BaseDto {
    private String id;
    private String loginId;
    private String name;
    private String pass;
    private List<RoleDto> roles;
}
