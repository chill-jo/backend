package com.example.surveyapp.global.config;

import com.example.surveyapp.global.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // @Secured, @PreAuthorize 사용 가능
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 비회원 허용
                        .requestMatchers(
                                "/api/register",
                                "/api/auth/login",
                                "/api/auth/logout",
                                "/api/auth/refresh",
                                "/error"
                        ).permitAll()

                        // 관리자 권한 필요
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 유저 권한 필요
                        .requestMatchers("/api/user/**").hasRole("USER")

                        // 나머지 인증 필요
                        .anyRequest().authenticated()
                )
                //UsernamePasswordAuthenticationFilter : 스프링 시큐리티에서 기본적으로
                // username, password와 같은 입력값을 이용해서 인증을 시도하고, 인증된 사용자 정보를
                // UsernamePasswrodAuthenticationToken으로 구성해서 SecurityContextHodler에 저장하는
                // UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)


                .build();
    }
}