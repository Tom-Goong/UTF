package com.yaoting.utf.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Lazy(false)
@Order(value = 1)
@Slf4j
public class SpringTool implements ApplicationContextAware, DisposableBean {
    private static ApplicationContext applicationContext = null;

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getApplicationContext().getBeansOfType(type);
    }

    public static void clearHolder() {
        if (log.isDebugEnabled()) {
            log.debug("Clear applicationContext:" + applicationContext);
        }
        applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringTool.applicationContext = context;
    }

    @Override
    public void destroy() {
        SpringTool.clearHolder();
    }

    public static void publish(ApplicationEvent event) {
        getApplicationContext().publishEvent(event);
    }
}
