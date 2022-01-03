package posmy.interview.boot.constant;

public class Constants {
    public static String TRANSACTION_RECORD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public enum Currency{
        MYR("MYR");

        String type;

        Currency(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum SubCurrency{
        CENT("CENT");

        String type;

        SubCurrency(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
