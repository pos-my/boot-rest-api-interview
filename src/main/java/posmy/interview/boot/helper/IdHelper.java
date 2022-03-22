package posmy.interview.boot.helper;

import java.util.UUID;

public class IdHelper {

    public static String generateId(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println("uuid: " + generateId());
    }
}
