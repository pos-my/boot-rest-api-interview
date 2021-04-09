package posmy.interview.boot.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import posmy.interview.boot.entity.Role;
import posmy.interview.boot.service.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posmy")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return new ResponseEntity<>(
            roleService.getRoles(),
            HttpStatus.OK
        );
    }
}
