package posmy.interview.boot.mockauth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_METHOD;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class, setupBefore = TEST_METHOD)
public @interface WithMockCustomUser {

    long id() default 1;

    boolean isLibrarian() default true;

    String username() default "librarian";

    String[] authorities() default {};

}
