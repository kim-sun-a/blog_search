package com.blog.search.controller;

import com.blog.search.model.Blog;
import com.blog.search.service.BlogSearchService;
import com.blog.search.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final BlogSearchService blogSearchService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/search")
    public ResponseEntity<Page<Blog>> getSearchBlogList(String keyword,
                                                              @PageableDefault(page = 1, size = 10, sort="accuracy") Pageable pageable) {
        return ResponseEntity.ok(blogSearchService.getBlogSearchList(keyword, pageable).block());
    }

    @GetMapping("/topKeyword")
    public ResponseEntity getTop10Keyword() {
        return ResponseEntity.ok(searchHistoryService.getTop10Keyword());
    }
}
