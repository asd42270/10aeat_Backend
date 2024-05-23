package com.final_10aeat.common.util.member;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMemberSecurityContextFactory.class)
public @interface WithMember {

    long id() default 0L;

    String email() default "";

    String password() default "";

    String name() default "";

    String role() default "";

    boolean isTermAgreed() default false;

    long officeId() default 0L;

    String officeName() default "";

    String address() default "";

    double mapX() default 0.0;

    double mapY() default 0.0;

    long buildingInfoId() default 0L;

    String buildingInfoDong() default "";

    String buildingInfoHo() default "";
}
