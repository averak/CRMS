package dev.abelab.crms.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.*;

@RequiredArgsConstructor
public class ConvertUtil {

    private static final Gson gson = new GsonBuilder().create();

    /**
     * Convert object to json string.
     *
     * @param object object
     *
     * @return string
     */
    public static String convertObjectToJson(final Object object) {
        return gson.toJson(object);
    }

    /**
     * Convert json to object
     *
     * @param <T>   type
     *
     * @param json  json
     *
     * @param clazz clazz
     *
     * @return object
     */
    public static <T> T convertJsonToObject(final String json, final Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

}
