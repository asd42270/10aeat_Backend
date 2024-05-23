package com.final_10aeat.common.util.admin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAdminSecurityContextFactory.class)
public @interface WithAdmin {

    long id() default 0L;

    String email() default "";

    String password() default "";
}

