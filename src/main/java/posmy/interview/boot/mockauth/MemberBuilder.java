package posmy.interview.boot.mockauth;

import posmy.interview.boot.entity.Member;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class MemberBuilder {

    private Long id = 0L;
    private String username = randomAlphabetic(10);
    private String password = randomAlphabetic(10);

    public static MemberBuilder sample() {
        return new MemberBuilder();
    }

    public Member build() {
        Member admin = new Member();
        admin.setId(id);
        admin.setUsername(username);
        admin.setPassword(password);

        admin.setToken(randomAlphanumeric(32));

        return admin;
    }

    public MemberBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MemberBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
}
