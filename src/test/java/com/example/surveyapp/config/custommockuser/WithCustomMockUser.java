package com.example.surveyapp.config.custommockuser;

import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface WithCustomMockUser {
    long id() default 1L;
    String email() default "test@example.com";
    String password() default "password123!";
    String name() default "홍길동";
    String nickname() default "kildong";
    UserRoleEnum role() default UserRoleEnum.SURVEYEE;
}