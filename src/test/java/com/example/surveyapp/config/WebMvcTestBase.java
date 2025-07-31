package com.example.surveyapp.config;

import com.example.surveyapp.domain.admin.controller.AdminController;
import com.example.surveyapp.domain.order.controller.OrderController;
import com.example.surveyapp.domain.point.controller.PointController;
import com.example.surveyapp.domain.product.controller.ProductController;
import com.example.surveyapp.domain.survey.controller.OptionsController;
import com.example.surveyapp.domain.survey.controller.QuestionController;
import com.example.surveyapp.domain.survey.controller.SurveyController;
import com.example.surveyapp.domain.user.controller.UserController;
import com.example.surveyapp.global.filter.JwtFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(
        controllers = {UserController.class, AdminController.class, OrderController.class,
                PointController.class, ProductController.class, OptionsController.class,
                QuestionController.class, SurveyController.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
)
@AutoConfigureMockMvc(addFilters = false)
public class WebMvcTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

}
