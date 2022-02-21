package posmy.interview.boot.Util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import posmy.interview.boot.Exception.GenericException;

import static posmy.interview.boot.Constant.*;

public final class JsonConverter {
    private static ObjectMapper objectMapper;

    private JsonConverter() {
    }

    public static <T> T fromString(String jsonString, Class<T> type) {
        T jsonObject = null;
        try {
            if (null == objectMapper) {
                objectMapper = newObjectMapper();
            }
            jsonObject = objectMapper.readValue(jsonString, type);
        } catch (JsonProcessingException e) {
            throw new GenericException(JSON_PARSE_OBJECT_ERROR, M_JSON_PARSE_OBJECT_ERROR, e);
        }
        return jsonObject;
    }

    public static String fromObject(Object object) {
        var jsonString = "";
        try {
            if (null == objectMapper) {
                objectMapper = newObjectMapper();
            }
            jsonString = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new GenericException(OBJECT_PARSE_JSON_ERROR, M_OBJECT_PARSE_JSON_ERROR, e);
        }
        return jsonString;
    }

    /**
     * @author JinPeng
     * Remove all the whitespace in the Json String
     * @param json, request body in Json format
     * @return Json String without white space
     */
    public static String removeWhitespaces(String json) {
        var quoted = false;

        var builder = new StringBuilder();

        int len = json.length();
        for (var i = 0; i < len; i++) {
            var c = json.charAt(i);
            if (c == '\"')
                quoted = !quoted;

            if (quoted || !Character.isWhitespace(c))
                builder.append(c);
        }

        return builder.toString();
    }

    private static ObjectMapper newObjectMapper() {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .registerModule(new JavaTimeModule())
                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

}
