package posmy.interview.boot;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public ModelMapper modelMapper() {
    	
    	ModelMapper modelMapper = new ModelMapper();
    	modelMapper.getConfiguration().setSkipNullEnabled(true);

        return modelMapper;
    }

}
