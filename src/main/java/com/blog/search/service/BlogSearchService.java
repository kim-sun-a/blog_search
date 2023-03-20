package com.blog.search.service;

import com.blog.search.error.ApiException;
import com.blog.search.model.Blog;
import com.blog.search.request.SearchDto;
import com.blog.search.response.ResponseApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static com.blog.search.code.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.blog.search.code.ErrorCode.INVALID_PARAMETER;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchService {
    private final SearchHistoryService searchHistoryService;
    @Value("${kakao.search.api.url}")
    private String searchUrl;
    @Value("${kakao.search.api.key}")
    private String apiKey;

    /**
     * blog 검색어에 따른 조회
     */
    public Mono<Page<Blog>> getBlogSearchList(String keyword, Pageable pageable) {
        if(StringUtils.isEmpty(keyword)) {
            throw new ApiException(INVALID_PARAMETER);
        }
        int page = (pageable.getPageNumber()==0) ? 0 : (pageable.getPageNumber()-1);
        PageRequest pageRequest = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        URI url = UriComponentsBuilder
                    .fromUriString(searchUrl) //기본 url
                    .queryParam("query", keyword) //검색어
                    .queryParam("sort", pageRequest.getSort())
                    .queryParam("page", page+1)
                    .queryParam("size", pageRequest.getPageSize())
                    .build()
                    .toUri();

        return WebClient.builder()
                .baseUrl(url.toString())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey)
                .build()
                .get()
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new ApiException(INVALID_PARAMETER)))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new ApiException(INTERNAL_SERVER_ERROR)))
                .bodyToMono(ResponseApi.class)
                .map(responseApi -> {
                    log.info("request Url: " + url.toString());
                    List<Blog> blogList = responseApi.getDocuments();
                    searchHistoryService.save(SearchDto.builder().keyword(keyword).build());
                    return new PageImpl<>(blogList, pageRequest, blogList.size());
                });
    }
}
