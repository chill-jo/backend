package com.example.surveyapp.domain.user.domain.repository;

import com.example.surveyapp.domain.user.domain.model.CategoryEnum;
import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.domain.user.domain.model.UserBaseData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBaseDataRepository extends JpaRepository<UserBaseData, Long> {

    Optional<UserBaseData> findByUserIdAndCategory(User userId, CategoryEnum category);

    List<UserBaseData> findAllByUserId(User userId);

}
