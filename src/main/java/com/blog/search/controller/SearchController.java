package com.blog.search.controller;

import com.blog.search.code.StatusCode;
import com.blog.search.model.Blog;
import com.blog.search.response.ResponseDto;
import com.blog.search.response.keywordRankResponse;
import com.blog.search.service.BlogSearchService;
import com.blog.search.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SearchController {
    private final BlogSearchService blogSearchService;
    private final SearchHistoryService searchHistoryService;

    @GetMapping("/search")
    public ResponseEntity<ResponseDto> getSearchBlogList(@RequestParam("keyword") @NotNull String keyword,
                                                         @PageableDefault(page = 1, size = 10, sort="accuracy") Pageable pageable) {
        Page<Blog> searchBlogList = blogSearchService.getBlogSearchList(keyword, pageable).block();
        ResponseDto responseDto = ResponseDto.builder().status(StatusCode.OK).message("정상 처리").data(searchBlogList).build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/topKeyword")
    public ResponseEntity<ResponseDto> getTop10Keyword() {
        List<keywordRankResponse> top10Keyword = searchHistoryService.getTop10Keyword();
        ResponseDto responseDto = ResponseDto.builder().status(StatusCode.OK).message("정상 처리").data(top10Keyword).build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
