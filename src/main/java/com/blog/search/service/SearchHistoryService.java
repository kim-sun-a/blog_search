package com.blog.search.service;

import com.blog.search.domain.SearchHistory;
import com.blog.search.error.ApiException;
import com.blog.search.repository.SearchHistoryRepository;
import com.blog.search.request.SearchDto;
import com.blog.search.response.keywordRankResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.blog.search.code.StatusCode.NO_DATA;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;

    /**
     * 검색시 검색어 저장
     */
    @Transactional
    public void save(SearchDto searchDto) {
        Optional<SearchHistory> findKeyword = searchHistoryRepository.findByKeyword(searchDto.getKeyword());
        if(findKeyword.isEmpty()) {         // 처음 검색된 키워드라면
            searchHistoryRepository.save(SearchHistory.builder().keyword(searchDto.getKeyword()).searchCnt(1).build());
        } else {                            // 검색된 기록이 있는 키워드라면
            findKeyword.get().increaseCount(1);
            searchHistoryRepository.save(findKeyword.get());
        }
    }


    /**
     * 검색어 top 10 조회
     */
    @Transactional(readOnly = true)
    public List<keywordRankResponse> getTop10Keyword() {
        List<keywordRankResponse> top10Keyword = searchHistoryRepository.getTop10Keyword();
        if(top10Keyword.isEmpty()) {
            throw new ApiException(NO_DATA);
        }
        return top10Keyword;
    }

}
