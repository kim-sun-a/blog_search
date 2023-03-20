package com.blog.search.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class SearchDto {
    private String keyword;         // 검색어
    private Integer count;

    @Builder
    public SearchDto(String keyword, Integer count) {
        this.keyword = keyword;
        this.count = count;
    }
}
