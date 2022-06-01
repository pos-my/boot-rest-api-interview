package posmy.interview.boot.comparator;

import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.ValueMatcherException;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexNullableValueMatcher<T> implements ValueMatcher<T> {
    private final Pattern expectedPattern;
    private final Boolean nullable;

    public RegexNullableValueMatcher(Boolean nullable) {
        this((String) null, nullable);
    }

    public RegexNullableValueMatcher(String pattern) {
        this(pattern, false);
    }

    public RegexNullableValueMatcher(String pattern, Boolean nullable) throws IllegalArgumentException {
        try {
            this.expectedPattern = pattern == null ? null : Pattern.compile(pattern);
            this.nullable = nullable;
        } catch (PatternSyntaxException var3) {
            throw new IllegalArgumentException("Constant expected pattern invalid: " + var3.getMessage(), var3);
        }
    }


    public boolean equal(T actual, T expected) {
        String actualString = actual.toString();
        String expectedString = expected.toString();

        try {
            Pattern pattern = this.isStaticPattern() ? this.expectedPattern : Pattern.compile(expectedString);
            //if allow null and the actual result also is null or the actual value = expected regex
            if (pattern.matcher(actualString).matches() || (nullable && actualString.equals("null"))) {
                return true;
            } else {
                throw new ValueMatcherException(this.getPatternType() + " expected pattern did not match value", pattern.toString(), actualString);
            }
        } catch (PatternSyntaxException e) {
            throw new ValueMatcherException(this.getPatternType() + " expected pattern invalid: " + e.getMessage(), e, expectedString, actualString);
        }
    }

    private boolean isStaticPattern() {
        return this.expectedPattern != null;
    }

    private String getPatternType() {
        return this.isStaticPattern() ? "Constant" : "Dynamic";
    }
}
