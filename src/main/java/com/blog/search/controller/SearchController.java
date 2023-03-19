package com.blog.search.controller;

import com.blog.search.error.ApiException;
import com.blog.search.model.Blog;
import com.blog.search.service.BlogSearchService;
import com.blog.search.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.blog.search.code.ErrorCode.INVALID_PARAMETER;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final BlogSearchService blogSearchService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/search")
    public ResponseEntity<Page<Blog>> getSearchBlogList(String keyword,
                                                        @PageableDefault(size = 10, sort="accuracy", page = 1) Pageable pageable) throws IllegalAccessException {
        if(StringUtils.isEmpty(keyword)) {
            throw new ApiException(INVALID_PARAMETER);
        }
        return ResponseEntity.ok(blogSearchService.getBlogSearchList(keyword, pageable));
    }

    @GetMapping("/topKeyword")
    public ResponseEntity getTop10Keyword() {
        return ResponseEntity.ok(searchHistoryService.getTop10Keyword());
    }
}
