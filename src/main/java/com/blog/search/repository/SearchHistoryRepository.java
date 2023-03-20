package com.blog.search.repository;

import com.blog.search.domain.SearchHistory;
import com.blog.search.response.keywordRankResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    @Query(nativeQuery = true,
            value = "select keyword, count(keyword) as keywordCnt from search_history group by keyword order by keywordCnt desc limit 0, 10")
    Optional<keywordRankResponse> getTop10Keyword();
}
