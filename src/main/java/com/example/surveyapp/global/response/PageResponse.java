package com.example.surveyapp.global.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
public class PageResponse<T> {
    private List<T> content;
    private Long totalElements;
    private int totalPages;
    private int size;
    private int number;

    public PageResponse(Page<T> page){
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.size = page.getSize();
        this.number = page.getNumber();
    }
}