package posmy.interview.boot;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Loadbooks {
	
	 private static final Logger log = LoggerFactory.getLogger(Loadbooks.class);
	
	@Bean
	  CommandLineRunner initBookShelf(BookShelf bookShelf) {

		log.info("Preloading " + bookShelf.save(new Book("Mathematics")));
    	log.info("Preloading " + bookShelf.save(new Book("Science")));
		
    	return null;
    	
//	    return args -> {
//	    	log.info("Preloading " + bookShelf.save(new Book("Mathematics")));
//	    	log.info("Preloading " + bookShelf.save(new Book("Science")));
//	    };
	  }
}
