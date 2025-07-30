package com.example.surveyapp.domain.order.controller;

import com.example.surveyapp.global.filter.JwtFilter;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@WebMvcTest(controllers = OrderControllerTest.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {
}
