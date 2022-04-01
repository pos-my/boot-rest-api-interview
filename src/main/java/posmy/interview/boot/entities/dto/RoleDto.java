package posmy.interview.boot.entities.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoleDto {
    private String id;

    private String name;

    private List<String> users;
}