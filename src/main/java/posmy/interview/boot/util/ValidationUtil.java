package posmy.interview.boot.util;

import posmy.interview.boot.exception.InvalidPaginationException;
import posmy.interview.boot.model.common.Pagination;

public class ValidationUtil {
    public static boolean isStringEmpty(String input){
        return input == null || input.replace(" ", "").length() == 0;
    }
}
