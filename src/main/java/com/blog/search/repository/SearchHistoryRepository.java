package com.blog.search.repository;

import com.blog.search.domain.SearchHistory;
import com.blog.search.response.keywordRankResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    @Query(nativeQuery = true,
            value = "select keyword, search_cnt as searchCnt from search_history order by searchCnt desc limit 0, 10")
    List<keywordRankResponse> getTop10Keyword();

    /**
     * 검색횟수를 해결하기 위한 비관적 락 조회 메서드
     * @param keyword
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SearchHistory> findByKeyword(String keyword);
}
