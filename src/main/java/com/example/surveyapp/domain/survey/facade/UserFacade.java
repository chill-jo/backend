package com.example.surveyapp.domain.survey.facade;

import com.example.surveyapp.domain.user.domain.model.User;

public interface UserFacade {
    User findUser(Long id);
}
