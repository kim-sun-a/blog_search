package com.blog.search.service;

import com.blog.search.model.Blog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BlogSearchServiceTest {
    @Autowired
    BlogSearchService blogSearchService;

    @Test
    @DisplayName("블로그 검색 조회")
    void blog_search_get() throws IllegalAccessException {
        // given
        String query = "집짓기";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("accuracy"));


        //when
        Page<Blog> blogSearchPage = blogSearchService.getBlogSearchList(query, pageable);

        //then
        assertEquals(10L, blogSearchPage.getContent().size());    // 첫페이지 사이즈 개수 확인
        assertEquals(1L, blogSearchPage.getNumber());             // 현재 페이지 번호
    }
}