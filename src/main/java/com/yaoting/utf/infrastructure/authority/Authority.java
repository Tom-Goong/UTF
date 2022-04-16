package com.yaoting.utf.infrastructure.authority;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authority {
    Role[] roles() default {};
}
