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
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
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
        SearchDto searchDto = SearchDto.builder().keyword("인기검색어").build();

        searchHistoryService.save(searchDto);

        assertEquals(1L, searchHistoryRepository.count());
        SearchHistory searchHistory = searchHistoryRepository.findAll().get(0);
        assertEquals("인기검색어", searchHistory.getKeyword());
        assertEquals(1, searchHistory.getSearchCnt());
    }


    @Test
    @DisplayName("동일한 저장시 검색횟수 증가 확인")
    void increase_keyword() {
        SearchDto searchDto1 = SearchDto.builder().keyword("인기검색어").build();
        SearchDto searchDto2 = SearchDto.builder().keyword("인기검색어").build();

        searchHistoryService.save(searchDto1);
        searchHistoryService.save(searchDto2);


        SearchHistory searchHistory = searchHistoryRepository.findByKeyword("인기검색어").get();
        assertEquals(1L, searchHistoryRepository.count());
        assertEquals("인기검색어", searchHistory.getKeyword());
        assertEquals(2, searchHistory.getSearchCnt());
    }


    @Test
    @DisplayName("검색어 목록 조회")
    void get_keywords() {
        // given
        for(int i=0; i<10; i++) {
            SearchDto searchDto = SearchDto.builder().keyword("인기검색어a").build();
            searchHistoryService.save(searchDto);
        }
        for(int i=0; i<12; i++) {
            SearchDto searchDto = SearchDto.builder().keyword("인기검색어b").build();
            searchHistoryService.save(searchDto);
        }
        for(int i=0; i<5; i++) {
            SearchDto searchDto = SearchDto.builder().keyword("인기검색어c").build();
            searchHistoryService.save(searchDto);
        }

        //when
        List<SearchHistory> findAll = searchHistoryRepository.findAll();

        //then
        assertEquals(3, findAll.size());
        assertEquals("인기검색어a", findAll.get(0).getKeyword());
        assertEquals(10, findAll.get(0).getSearchCnt());
    }

    @Test
    @DisplayName("검색어 순위 조회")
    void top_10_keyword() {
        // given
        for(int i=0; i<10; i++) {
            SearchDto searchDto = SearchDto.builder().keyword("인기검색어a").build();
            searchHistoryService.save(searchDto);
        }
        for(int i=0; i<12; i++) {
            SearchDto searchDto = SearchDto.builder().keyword("인기검색어b").build();
            searchHistoryService.save(searchDto);
        }
        for(int i=0; i<5; i++) {
            SearchDto searchDto = SearchDto.builder().keyword("인기검색어c").build();
            searchHistoryService.save(searchDto);
        }

        //when
        List<keywordRankResponse> top10 = searchHistoryService.getTop10Keyword();

        //then
        assertEquals(3, top10.size());
        assertEquals(12, top10.get(0).getSearchCnt());
    }


    private final AtomicLong count = new AtomicLong(0);
    @Test
    @DisplayName("동시에 검색어 입력")
    void input_sameTIme_keyword() throws InterruptedException {

        //given
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(100);

        //when
        for(int i = 0; i<100; i++) {
            executorService.submit(() -> execute(SearchDto.builder().keyword("A").build(), latch));    // A 검색시 검색횟수가 1회 추가
        }
        latch.await();

    }

    private synchronized void execute(SearchDto searchHistory, CountDownLatch countDownLatch) {
        try {
            searchHistoryService.save(searchHistory);
            SearchHistory byKeyword = searchHistoryRepository.findByKeyword(searchHistory.getKeyword()).get();
            long result = count.updateAndGet((value) ->  byKeyword.getSearchCnt());
            System.out.println("result = " + result);

        } finally {
            countDownLatch.countDown();
        }

    }
}