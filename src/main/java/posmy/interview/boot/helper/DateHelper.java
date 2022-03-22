package posmy.interview.boot.helper;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static final String ASIA_KL = "Asia/Kuala_Lumpur";

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static String displayDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone(ASIA_KL));
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) {

    }
}
