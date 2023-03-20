package com.blog.search.service;

import com.blog.search.error.ApiException;
import com.blog.search.model.Blog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BlogSearchServiceTest {
    @Autowired
    BlogSearchService blogSearchService;

    @Test
    @DisplayName("블로그 검색 조회")
    void blog_search_get() {
        // given
        String query = "집짓기";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("accuracy"));


        //when
        Mono<Page<Blog>> blogSearchPage = blogSearchService.getBlogSearchList(query, pageable);

        //then
        assertEquals(10L, blogSearchPage.block().getContent().size());    // 첫페이지 사이즈 개수 확인
        assertEquals(0, blogSearchPage.block().getNumber());             // 현재 페이지 번호
    }


    @Test()
    @DisplayName("블로그 검색시 키워드가 없으면 안된다")
    void blog_search_vail_keyword() {
        // given
        String query = "";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("accuracy"));

        //then
        assertThrows(ApiException.class, () -> {
            //when
            Mono<Page<Blog>> blogSearchPage = blogSearchService.getBlogSearchList(query, pageable);
        });
    }
}