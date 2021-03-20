package posmy.interview.boot;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import posmy.interview.boot.repository.LibrarianRepository;
import posmy.interview.boot.repository.MemberRepository;

@Rollback
// @RunWith(SpringRunner.class)
// @TestPropertySource("classpath:application-test.properties")
public class BaseTest {

    @Autowired
    protected LibrarianRepository librarianRepository;
    @Autowired
    protected MemberRepository memberRepository;
}
