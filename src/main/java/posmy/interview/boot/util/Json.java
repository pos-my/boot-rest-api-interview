package posmy.interview.boot.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toString(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e){
            return "";
        }
    }
}
