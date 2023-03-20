package com.blog.search.service;

import com.blog.search.domain.SearchHistory;
import com.blog.search.repository.SearchHistoryRepository;
import com.blog.search.request.SearchDto;
import com.blog.search.response.keywordRankResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;

    /**
     * 검색시 검색어 저장
     */
    public void save(SearchDto searchDto) {
        SearchHistory searchHistory = SearchHistory.builder().keyword(searchDto.getKeyword()).searchDate(searchDto.getSearchDate()).build();
        searchHistoryRepository.save(searchHistory);
    }


    /**
     * 검색어 top 10 조회
     */
    @Transactional(readOnly = true)
    public List<keywordRankResponse> getTop10Keyword() {
        return searchHistoryRepository.getTop10Keyword();
    }

}
