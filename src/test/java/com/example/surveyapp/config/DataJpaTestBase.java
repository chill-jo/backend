package com.example.surveyapp.config;

import com.example.surveyapp.global.config.JpaConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(JpaConfig.class)
@DataJpaTest
@ActiveProfiles("test")
public class DataJpaTestBase {
}
