package com.blog.search.service;

import com.blog.search.model.Blog;
import com.blog.search.request.SearchDto;
import com.blog.search.response.ResponseApi;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogSearchService {
    private final SearchHistoryService searchHistoryService;
    private final RestTemplate restTemplate;
    @Value("${kakao.search.api.url}")
    private String searchUrl;
    @Value("${kakao.search.api.key}")
    private String apiKey;

    /**
     * blog 검색어에 따른 조회
     */
    public Page<Blog> getBlogSearchList(String keyword, Pageable pageable) throws IllegalAccessException {
        List<Blog> blogList = new ArrayList<>();
        URI url = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK "+apiKey);
            HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
            url = UriComponentsBuilder
                    .fromUriString(searchUrl) //기본 url
                    .queryParam("query", keyword) //검색어
                    .queryParam("sort", pageable.getSort())
                    .queryParam("page", pageable.getPageNumber())
                    .queryParam("size", pageable.getOffset())
                    .build()
                    .encode(StandardCharsets.UTF_8) //인코딩
                    .toUri();
            ResponseEntity<ResponseApi> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>(){} //반환되는 데이터타입
            );
            if (HttpStatus.OK.equals(responseEntity.getStatusCode()) && responseEntity.hasBody()) {
                blogList = Objects.requireNonNull(responseEntity.getBody(), "내용이 없습니다.").getDocuments();
                searchHistoryService.save(new SearchDto(keyword, new Date()));  // 검색시 키워드 저장
                log.info("blog list : "+blogList);
            }
            // 응답 status 실패인 경우 request httpEntity, responseEntity 디버그 로깅
            if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                log.info("1. request - [{}] {}", url, httpEntity);
                log.info("2. response - {}", responseEntity);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        log.info("[{}] response BlogList size : {}", url, blogList.size());

        return new PageImpl<>(blogList, pageable, blogList.size());
    }


}
