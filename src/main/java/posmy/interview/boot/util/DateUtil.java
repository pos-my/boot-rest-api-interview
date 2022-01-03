package posmy.interview.boot.util;

import posmy.interview.boot.constant.Constants;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {
    public static String KL_TIME_ZONE = "Asia/Kuala_Lumpur";
    public static String STANDARD_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final SimpleDateFormat standardDateFormat = new SimpleDateFormat(STANDARD_DATE_FORMAT);
    private static final TimeZone malaysianTimeZone = TimeZone.getTimeZone(KL_TIME_ZONE);

    public static String convertToStandardDateStringFormat(long timestamp) {
        standardDateFormat.setTimeZone(malaysianTimeZone);
        java.util.Date dateObj = new java.util.Date(timestamp);
        return standardDateFormat.format(dateObj.getTime());
    }
}
