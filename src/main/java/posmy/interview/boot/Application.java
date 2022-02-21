package posmy.interview.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import posmy.interview.boot.Model.Repository.UsersRepository;

@SpringBootApplication
public class Application  {

    @Autowired
    UsersRepository usersRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
