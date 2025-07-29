package com.example.surveyapp.domain.user.domain.repository;

import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserBaseData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBaseDataRepository extends JpaRepository<UserBaseData, Long> {

    List<UserBaseData> findAllByUserId(User userId);

}
