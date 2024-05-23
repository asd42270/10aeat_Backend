package com.final_10aeat.common.util.manager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithManagerSecurityContextFactory.class)
public @interface WithManager {

    long id() default 0L;

    String email() default "";

    String password() default "";

    String name() default "";

    String phoneNumber() default "";

    String lunchBreakStart() default "";

    String lunchBreakEnd() default "";

    String managerOffice() default "";

    String affiliation() default "";

    String role() default "";

    long officeId() default 0L;

    String officeName() default "";

    String address() default "";

    double mapX() default 0.0;

    double mapY() default 0.0;
}

