package posmy.interview.boot.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
public class RoleDto extends BaseDto {
    private String id;
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserDto> users;
}
