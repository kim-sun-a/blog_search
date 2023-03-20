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
    private Integer searchCnt;

    @Builder
    public SearchDto(String keyword, Integer searchCnt) {
        this.keyword = keyword;
        this.searchCnt = searchCnt;
    }
}
