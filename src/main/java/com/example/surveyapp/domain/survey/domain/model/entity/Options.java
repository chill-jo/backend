package com.example.surveyapp.domain.survey.domain.model.entity;

import com.example.surveyapp.global.config.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "options" )
public class Options extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private Long number;

    @Column(nullable = false, length = 255)
    private String content;

    public Options(Question question, Long number, String content){
        this.question = question;
        this.number = number;
        this.content = content;
    }
    public void changeNumber(Long number){
        this.number = number;
    }
    public void changeContent(String content){
        this.content = content;
    }
    public boolean isFromQuestion(Question question){
        return this.question.equals(question);
    }
}
