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
    private Date searchDate;        // 검색날짜

    @Builder
    public SearchDto(String keyword, Date searchDate) {
        this.keyword = keyword;
        this.searchDate = searchDate;
    }
}
