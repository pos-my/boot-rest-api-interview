package posmy.interview.boot.mockauth;

import posmy.interview.boot.entity.Librarian;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class LibrarianBuilder {

    private Long id = 0L;
    private String username = randomAlphabetic(10);
    private String password = randomAlphabetic(10);
    private String token;

    public static LibrarianBuilder sample() {
        return new LibrarianBuilder();
    }

    public Librarian build() {
        Librarian admin = new Librarian();
        admin.setId(id);
        admin.setUsername(username);
        admin.setPassword(password);

        admin.setToken(randomAlphanumeric(32));

        return admin;
    }

    public LibrarianBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LibrarianBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
}
