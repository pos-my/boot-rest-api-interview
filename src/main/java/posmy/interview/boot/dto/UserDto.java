package posmy.interview.boot.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id",
        scope = UserDto.class)
public class UserDto extends BaseDto {
    private String id;
    private String loginId;
    private String name;
    private String pass;
    private List<RoleDto> roles;
    private List<BookDto> borrowedBooks;
}
