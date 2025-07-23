package com.example.surveyapp.domain.survey.service;

import com.example.surveyapp.domain.survey.domain.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    //설문 상태 확인하는거 만들어야할듯
    //해당 설문 출제자인지 확인해야함

    public void createQuestion(){

    }

    public void updateQuestion(){

    }

    public void deleteQuestion(){

    }
}
