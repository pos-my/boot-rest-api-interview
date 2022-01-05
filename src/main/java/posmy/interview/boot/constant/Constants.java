package posmy.interview.boot.constant;

public class Constants {
    public static String TRANSACTION_RECORD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String ROLE_LIBRARIAN = "ROLE_LIBRARIAN";
    public static String ROLE_MEMBER = "ROLE_MEMBER";

    public enum Currency {
        MYR("MYR");

        String type;

        Currency(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum SubCurrency {
        CENT("CENT");

        String type;

        SubCurrency(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }


    public enum BookStatus {
        AVAILABLE("AVAILABLE"),
        BORROWED("BORROWED"),
        DELETED("DELETED");

        String type;

        BookStatus(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static boolean isStatusValid(String type){
            String result = "";
            for (BookStatus status : BookStatus.values()) {
                if (status.type.equals(type)){
                    result = status.type;
                    break;
                }
            }
            return result.length() > 0;
        }
    }

    public enum TransactionStatus {
        BORROWED("BORROWED"),
        RETURNED("RETURNED"),
        REMOVED("REMOVED");;

        String type;

        TransactionStatus(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum UserStatus {
        ACTIVATED("ACTIVATED"),
        REMOVED("REMOVED");

        String type;

        UserStatus(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static boolean isStatusValid(String type){
            String result = "";
            for (BookStatus status : BookStatus.values()) {
                if (status.type.equals(type)){
                    result = status.type;
                    break;
                }
            }
            return result.length() > 0;
        }
    }

    public enum Role {
        ROLE_LIBRARIAN("ROLE_LIBRARIAN"),
        ROLE_MEMBER("ROLE_MEMBER"),
        ROLE_ANONYMOUS("ROLE_ANONYMOUS");

        String type;

        Role(String type){
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static boolean isRoleValid(String type){
            String result = "";
            for (Role status : Role.values()) {
                if (status.type.equals(type)){
                    result = status.type;
                    break;
                }
            }
            return result.length() > 0;
        }
    }

}
