package com.example.surveyapp.config.TestMockBeans;

import com.example.surveyapp.domain.admin.service.AdminService;
import com.example.surveyapp.domain.order.service.OrderService;
import com.example.surveyapp.domain.point.service.PointService;
import com.example.surveyapp.domain.product.service.ProductService;
import com.example.surveyapp.domain.survey.service.OptionsService;
import com.example.surveyapp.domain.survey.service.QuestionService;
import com.example.surveyapp.domain.survey.service.SurveyService;
import com.example.surveyapp.domain.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestMockBeans {
    @Bean public UserService userService() {return mock(UserService.class);}
    @Bean public AdminService adminService() {return mock(AdminService.class);}
    @Bean public OrderService orderService() {return mock(OrderService.class);}
    @Bean public PointService pointService() {return mock(PointService.class);}
    @Bean public ProductService productService() {return mock(ProductService.class);}
    @Bean public OptionsService optionsService() {return mock(OptionsService.class);}
    @Bean public QuestionService questionService() {return mock(QuestionService.class);}
    @Bean public SurveyService surveyService() {return mock(SurveyService.class);}
}
