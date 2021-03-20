package posmy.interview.boot.fixture;

import posmy.interview.boot.dto.ICreateDto;
import posmy.interview.boot.dto.IDto;
import posmy.interview.boot.dto.IUpdateDto;
import posmy.interview.boot.entity.Librarian;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class LibrarianBuilder implements IBuilder<Librarian, Long> {

    private Long id;
    private String username = randomAlphabetic(10);
    private String password = randomAlphabetic(10);
    private String token = randomAlphabetic(32);

    public static LibrarianBuilder sample() {
        return new LibrarianBuilder();
    }

    @Override
    public Librarian build() {
        Librarian librarian = new Librarian();
        librarian.setUsername(username);
        librarian.setPassword(password);
        librarian.setToken(token);
        return librarian;
    }

    @Override
    public IDto buildDto() {
        return null;
    }

    @Override
    public ICreateDto buildCreateDto() {
        return null;
    }

    @Override
    public IUpdateDto buildUpdateDto() {
        return null;
    }
}
