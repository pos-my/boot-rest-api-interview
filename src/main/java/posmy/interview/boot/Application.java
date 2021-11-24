package posmy.interview.boot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import posmy.interview.boot.domain.User;
import posmy.interview.boot.enums.UserRole;
import posmy.interview.boot.enums.UserStatus;
import posmy.interview.boot.manager.UserManager;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner initialLoad(UserManager userService) {
        return (args) -> {
            //Default User
            userService.addMember(User.builder().username("librarian").password("p@ssw0rd").role(UserRole.LIBRARIAN).status(UserStatus.ACTIVE).build());
            userService.addMember(User.builder().username("member").password("p@ssw0rd").role(UserRole.MEMBER).status(UserStatus.ACTIVE).build());
        };
    }
}
