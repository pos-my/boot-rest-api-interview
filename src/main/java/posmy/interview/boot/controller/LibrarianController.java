package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.entity.LibrarianEntity;
import posmy.interview.boot.exception.NoDataFoundException;
import posmy.interview.boot.model.CreateLibrarianRequest;
import posmy.interview.boot.model.Librarian;
import posmy.interview.boot.service.librarian.LibrarianService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/librarian")
public class LibrarianController {

    private final LibrarianService librarianService;

    @GetMapping("/{libId}")
    public ResponseEntity<Librarian> getLibrarian(@PathVariable("libId") long libId) throws NoDataFoundException {
        return ResponseEntity.ok(librarianService.getLibrarian(libId));
    }

    @PostMapping
    public ResponseEntity<LibrarianEntity> createNewLibrarian(@RequestBody CreateLibrarianRequest createLibrarianRequest) {
        return ResponseEntity.ok(librarianService.saveLibrarian(createLibrarianRequest));
    }
}
