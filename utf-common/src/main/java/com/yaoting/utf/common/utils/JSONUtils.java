package com.yaoting.utf.common.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class JSONUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    // 将对象转成字符串
    public static String objectToString(Object obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }
    // 将Map转成指定的Bean
    public static <T> T mapToBean(Map map, Class<T> clazz) throws Exception {
        return mapper.readValue(objectToString(map), clazz);
    }
    // 将Bean转成Map
    public static Map beanToMap(Object obj) throws Exception {
        return mapper.readValue(objectToString(obj), Map.class);
    }
    //将string转换成对象列表
    public static <T> List<T> stringToBeanList(String string, Class<T> clazz) throws Exception {
        return mapper.readValue(string, mapper.getTypeFactory().constructParametricType(List.class, clazz));
    }
    //将string转换成对象
    public static <T> T stringToBean(String string, Class<T> clazz) throws Exception {
        return mapper.readValue(string, clazz);
    }
    //将string转换成Map
    public static Map stringToMap(String string) throws Exception {
        return mapper.readValue(string, Map.class);
    }
}
