package posmy.interview.boot.helper;

import java.util.InvalidPropertiesFormatException;
import java.util.regex.Pattern;

public class ValidationHelper {

    private static final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    public static void isEmailValid(String email) throws InvalidPropertiesFormatException {
        if(!emailPattern.matcher(email).matches()){
            throw new InvalidPropertiesFormatException("Invalid Email");
        }
    }
}
