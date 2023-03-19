package com.blog.search.service;

import com.blog.search.domain.SearchHistory;
import com.blog.search.repository.SearchHistoryRepository;
import com.blog.search.request.SearchDto;
import com.blog.search.response.keywordRankResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SearchHistoryServiceTest {
    @Autowired
    SearchHistoryService searchHistoryService;
    @Autowired
    SearchHistoryRepository searchHistoryRepository;
    @BeforeEach
    void clean() {
        searchHistoryRepository.deleteAll();}

    @Test
    @DisplayName("검색어 저장")
    void save_keyword() {
        SearchDto searchDto = SearchDto.builder().keyword("인기 검색어").searchDate(new Date()).build();

        searchHistoryService.save(searchDto);

        assertEquals(1L, searchHistoryRepository.count());
        SearchHistory searchHistory = searchHistoryRepository.findAll().get(0);
        assertEquals("인기 검색어", searchHistory.getKeyword());

    }


    @Test
    @DisplayName("검색어 목록 조회")
    void get_keywords() {
        // given
        List<SearchHistory> searchDtoListA = IntStream.range(0,10)
                .mapToObj(i ->  SearchHistory.builder()
                        .keyword("인기 검색어 a")
                        .searchDate(new Date())
                        .build())
                .collect(Collectors.toList());
        searchHistoryRepository.saveAll(searchDtoListA);
        List<SearchHistory> searchDtoListB = IntStream.range(0,5)
                .mapToObj(i ->  SearchHistory.builder()
                        .keyword("인기 검색어 b")
                        .searchDate(new Date())
                        .build())
                .collect(Collectors.toList());
        searchHistoryRepository.saveAll(searchDtoListB);
        List<SearchHistory> searchDtoListC = IntStream.range(0,3)
                .mapToObj(i ->  SearchHistory.builder()
                        .keyword("인기 검색어 c")
                        .searchDate(new Date())
                        .build())
                .collect(Collectors.toList());
        searchHistoryRepository.saveAll(searchDtoListC);

        //when
        List<SearchHistory> findAll = searchHistoryRepository.findAll();

        //then
        assertEquals(18, findAll.size());
        assertEquals("인기 검색어 a", findAll.get(0).getKeyword());
    }

    @Test
    @DisplayName("검색어 순위 조회")
    void top_10_keyword() {
        // given
        List<SearchHistory> searchDtoListA = IntStream.range(0,10)
                .mapToObj(i ->  SearchHistory.builder()
                        .keyword("인기 검색어 a")
                        .searchDate(new Date())
                        .build())
                .collect(Collectors.toList());
        searchHistoryRepository.saveAll(searchDtoListA);
        List<SearchHistory> searchDtoListB = IntStream.range(0,5)
                .mapToObj(i ->  SearchHistory.builder()
                        .keyword("인기 검색어 b")
                        .searchDate(new Date())
                        .build())
                .collect(Collectors.toList());
        searchHistoryRepository.saveAll(searchDtoListB);
        List<SearchHistory> searchDtoListC = IntStream.range(0,3)
                .mapToObj(i ->  SearchHistory.builder()
                        .keyword("인기 검색어 c")
                        .searchDate(new Date())
                        .build())
                .collect(Collectors.toList());
        searchHistoryRepository.saveAll(searchDtoListC);

        //when
        List<keywordRankResponse> top10 = searchHistoryService.getTop10Keyword();

        //then
        assertEquals(3, top10.size());
        assertEquals(10, top10.get(0).getKeywordCnt());
    }
}