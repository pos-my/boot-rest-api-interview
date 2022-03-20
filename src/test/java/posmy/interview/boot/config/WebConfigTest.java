package posmy.interview.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@TestConfiguration
@Slf4j
@EnableWebSecurity
//@ComponentScan(basePackages = { "posmy.interview.boot.config" })
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfigTest extends WebSecurityConfigurerAdapter {
}
