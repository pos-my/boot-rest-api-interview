package posmy.interview.boot.unit_test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import posmy.interview.boot.BaseTest;
import posmy.interview.boot.entity.Librarian;
import posmy.interview.boot.fixture.LibrarianBuilder;
import posmy.interview.boot.mockauth.WithMockCustomUser;
import posmy.interview.boot.services.LibrarianService;

import static org.junit.Assert.fail;

/*
    This test quite ugly :P
 */
@SpringBootTest
@ContextConfiguration
public class LibrarianServiceTest extends BaseTest {

    @Autowired
    private LibrarianService librarianService;

    private Librarian admins;

    @BeforeEach
    public void setup() {
        // we manually create librarian by default
        // fixme: we bypass our hash algorithm for password
        admins = librarianRepository.save(LibrarianBuilder.sample().build());
    }

    @WithMockCustomUser(authorities = {"LIBRARIAN_CREATE"})
    @Test
    public void createTest() {
        try {
            librarianService.create(LibrarianBuilder.sample().build());
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail();
    }

    @WithMockCustomUser(authorities = {"LIBRARIAN_CREATE, LIBRARIAN_UPDATE"})
    @Test
    public void updateTest() {
        try {
            librarianService.update(admins);
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail();
    }

    @WithMockCustomUser(authorities = {"LIBRARIAN_CREATE", "LIBRARIAN_DELETE"})
    @Test
    public void deleteTest() {
        try {
            librarianService.delete(admins.getId());
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail();
    }
}
