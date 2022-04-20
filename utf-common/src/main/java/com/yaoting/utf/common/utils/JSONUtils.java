package com.yaoting.utf.common.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class JSONUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String objectToString(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T mapToBean(Map map, Class<T> clazz) throws Exception {
        return mapper.readValue(objectToString(map), clazz);
    }

    public static Map beanToMap(Object obj) throws Exception {
        return mapper.readValue(objectToString(obj), Map.class);
    }

    public static <T> List<T> stringToBeanList(String string, Class<T> clazz) throws Exception {
        return mapper.readValue(string, mapper.getTypeFactory().constructParametricType(List.class, clazz));
    }

    public static <T> T stringToBean(String string, Class<T> clazz) throws Exception {
        return mapper.readValue(string, clazz);
    }

    public static Map stringToMap(String string) throws Exception {
        return mapper.readValue(string, Map.class);
    }
}
