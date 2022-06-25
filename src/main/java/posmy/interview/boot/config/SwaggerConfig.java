package posmy.interview.boot.config;

import java.io.FileReader;
import java.io.IOException;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Hafiz
 */
@Profile("!prod")
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "posmy.interview.boot.api")
public class SwaggerConfig {

    /**
     *
     * @return Docket
     */
    @Bean
    public Docket api() throws IOException, XmlPullParserException {

        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        return new Docket(DocumentationType.SWAGGER_2)  
          .select() 
          .apis(RequestHandlerSelectors.basePackage("posmy.interview.boot.api"))
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(
        		  new ApiInfoBuilder().title("POS-MY - Java Interview Assignment").description("Spring Boot application that will be used by our candidates to implement interview assignment.")
        	        .termsOfServiceUrl("")
                    .contact(new Contact("Developers", "muhammad.alhafiz00@gmail.com", ""))
                    .license("Open Source")
                    .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
        	        .version(model.getParent().getVersion())
        	        .build());
    }
}
