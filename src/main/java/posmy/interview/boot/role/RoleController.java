package posmy.interview.boot.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import posmy.interview.boot.exceptions.RecordNotFoundException;

@RestController
@RequestMapping(path = "/roles")
public class RoleController {

    @Autowired
    private RoleService service;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> viewAll() {
        return ResponseEntity.ok(service.viewAll());
    }

    @GetMapping(value = "/{name}")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<?> view(@PathVariable String name) throws RecordNotFoundException {
        return ResponseEntity.ok(service.view(name));
    }

}