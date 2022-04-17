package com.yaoting.utf.common.utils;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BeanUtils {

    @SneakyThrows
    public static <T> T to(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }

        T t = clazz.newInstance();
        org.springframework.beans.BeanUtils.copyProperties(source, t);
        return t;
    }

    @SneakyThrows
    public static <T> List<T> toList(List<?> sources, Class<T> clazz) {
        return sources.stream()
                .map(ele -> BeanUtils.to(ele, clazz))
                .collect(Collectors.toList());
    }

    public static void copyProperties(Object source, Object target) throws BeansException {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }
}
