package com.example.surveyapp.domain.user.domain.model;

import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum userRole;

    private boolean isDeleted = false;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, String password, String name, String nickname, UserRoleEnum userRole) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    private void delete(){
        this.isDeleted = true;
    }
}
