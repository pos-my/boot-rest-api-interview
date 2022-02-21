package posmy.interview.boot;

public final class Constant {

    private Constant() {
    }

    public static final String MDC_TRANS_ID = "TransId";
    public static final String AVAILABLE = "AVAILABLE";
    public static final String BORROWED = "BORROWED";

    public static final String JSON_PARSE_OBJECT_ERROR = "500";
    public static final String OBJECT_PARSE_JSON_ERROR = "500";
    public static final String MDC_TRANS_MISSING = "1000";
    public static final String BOOK_ID_NOT_FOUND = "1001";
    public static final String USERNAME_NOT_FOUND = "1002";
    public static final String UNABLE_DELETE_OTHERS_ACCOUNT = "1003";
    public static final String UNCAUGHT_EXCEPTION = "9999";

    public static final String M_JSON_PARSE_OBJECT_ERROR = "JSON parse to object error";
    public static final String M_OBJECT_PARSE_JSON_ERROR = "Object parse to JSON error";
    public static final String M_MDC_TRANS_MISSING = "Missing requestId in the request";
    public static final String M_BOOK_ID_NOT_FOUND = "Book Id is not exist";
    public static final String M_USERNAME_NOT_FOUND = "Username is not exist";
    public static final String M_UNABLE_DELETE_OTHERS_ACCOUNT = "Member can only delete own account";
    public static final String M_UNCAUGHT_EXCEPTION = "Uncaught Exception happened";
}
