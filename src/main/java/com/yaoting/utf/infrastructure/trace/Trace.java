package com.yaoting.utf.infrastructure.trace;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trace {
    String value() default "";
}
