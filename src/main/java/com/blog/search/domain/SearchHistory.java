package com.blog.search.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;         // 검색어
    private Integer count;          // 검색횟수

    // ======= 비즈니스 로직 ========
    public void increaseCount(final Integer count) {
        this.count = this.count + count;
    }
}
