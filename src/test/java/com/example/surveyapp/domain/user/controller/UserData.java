package com.example.surveyapp.domain.user.controller;

import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserRoleEnum;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class UserData {
    public static User createDummyUser(Long id) {
        User user = User.of(
                "dummy@example.com",
                "password",
                "홍길동",
                "gildong",
                UserRoleEnum.SURVEYEE
        );
        setField(user, "id", id);

        return user;
    }
}
