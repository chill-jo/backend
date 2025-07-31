package com.example.surveyapp.config.generator;

import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;

public class UserFixtureGenerator {
    public static final Long ID = 1L;
    public static final String EMAIL = "test@example.com";
    public static final String PASSWORD = "password123!";
    public static final String NAME = "홍길동";
    public static final String NICKNAME = "gildong";
    public static final UserRoleEnum ROLE = UserRoleEnum.SURVEYEE;

    public static User generateUserFixture() {
        User user = User.of(EMAIL, PASSWORD, NAME, NICKNAME, ROLE);
        return user;
    }
}
