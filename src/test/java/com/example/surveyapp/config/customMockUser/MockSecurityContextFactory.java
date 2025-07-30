package com.example.surveyapp.config.customMockUser;

import com.example.surveyapp.domain.user.domain.model.User;
import com.example.surveyapp.global.security.jwt.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.util.ReflectionTestUtils;

public class MockSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        User mockUser = User.of(
                annotation.email(),
                annotation.password(),
                annotation.name(),
                annotation.nickname(),
                annotation.role()
        );
        ReflectionTestUtils.setField(mockUser, "id", annotation.id());

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        return context;
    }
}
