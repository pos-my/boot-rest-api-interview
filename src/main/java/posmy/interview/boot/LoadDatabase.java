package posmy.interview.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import posmy.interview.boot.persistence.*;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    @Qualifier("members")
    private MemberRepository memberRepository;

    @Autowired
    @Qualifier("books")
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        memberRepository.save(new Member("Jackie", Role.LIBRARIAN, "{noop}Pwd1"));
        memberRepository.save(new Member("Donnie", Role.MEMBER, "{noop}Pwd2"));
        memberRepository.findAll().forEach(member -> log.info("Preloaded " + member));

        bookRepository.save(new Book("The Theory of Everything", Status.AVAILABLE));
        bookRepository.save(new Book("Law of Attraction", Status.AVAILABLE));
        bookRepository.findAll().forEach(book -> log.info("Preloaded " + book));
    }
}


//@Configuration
//class LoadDatabase {
//
//    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
//
//    @Bean
//    CommandLineRunner initDatabase(MemberRepository memberRepository) {
//
//        return args -> {
//            memberRepository.save(new Member("Jackie", "Chan", Role.LIBRARIAN, "Pwd1"));
//            memberRepository.save(new Member("Donnie", "Yen", Role.MEMBER, "Pwd2"));
//            memberRepository.findAll().forEach(member -> log.info("Preloaded " + member));
//        };
//    }
//}

