package posmy.interview.boot.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Configuration
public class CorsConfig {

    @Value("${app.security.cors.allowed_origins}")
    private String allowedOrigins;

    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", createConfig());
        return source;
    }

    /**
     * fixme: This is too ugly
     *
     * @return
     */
    private CorsConfiguration createConfig() {
        String[] allowOrigins = getAllowedOrigins();

        // Allow any origin by default
        if (allowOrigins.length == 1 && EMPTY.equals(allowOrigins[0])) {
            allowOrigins = new String[]{"*"};
        }

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(new ArrayList<>(Arrays.asList(allowOrigins)));
        configuration.setAllowedMethods(new ArrayList<>(Arrays.asList(allowOrigins)));
        configuration.setAllowedHeaders(new ArrayList<>(Arrays.asList(allowOrigins)));
        return configuration;
    }

    public String[] getAllowedOrigins() {
        assert allowedOrigins != null;
        return allowedOrigins.split(",");
    }
}
