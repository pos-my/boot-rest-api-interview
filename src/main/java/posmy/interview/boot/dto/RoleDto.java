package posmy.interview.boot.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.List;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id",
        scope = RoleDto.class)
public class RoleDto extends BaseDto {
    private String id;
    private String name;
    private List<UserDto> users;
}
