package com.blog.search.controller;

import com.blog.search.repository.SearchHistoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SearchHistoryControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    SearchHistoryRepository searchHistoryRepository;

    @Test
    @DisplayName("블로그 검색 조회")
    void get_blog_test() throws Exception  {
        String keyword = "집짓기";
        mockMvc.perform(get("/search?keyword="+keyword+"&page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(10)))
                .andDo(print());
    }

    @Test
    @DisplayName("블로그 검색 조회시 검색어는 필수다")
    void get_blog_keyword_vail_test() throws Exception  {
        String keyword = "";
        mockMvc.perform(get("/search?keyword="+keyword+"&page=1&size=10"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


    @Test()
    @DisplayName("블로그 검색시 페이지 옵션이 없어도 검색이 가능하다")
    void blog_search_vail_page() throws Exception {
        String keyword = "닭가슴살";

        mockMvc.perform(get("/search?keyword="+keyword))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("블로그 최신순 검색")
    void get_blog_search_Sorting() throws Exception  {
        String keyword = "집짓기";
        mockMvc.perform(get("/search?keyword="+keyword+"&sort=recency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(10)))
                .andExpect(jsonPath("$.content[0].title", is("부산발도르프학교 3학년 <b>집</b><b>짓기</b>")))
                .andDo(print());
    }


    @Test
    @DisplayName("블로그 검색 조회시 1페이지는 제일 첫페이지이다")
    void get_blog_first_page() throws Exception  {
        String keyword = "집짓기";
        mockMvc.perform(get("/search?keyword="+keyword+"&page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(10)))
                .andExpect(jsonPath("$.first", is(true)))
                .andDo(print());
    }

    @Test
    @DisplayName("인기 검색어 조회")
    void get_top10_keyword_test() throws Exception  {
        mockMvc.perform(get("/topKeyword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].keyword", is("인기검색어 F")))
                .andExpect(jsonPath("$[0].keywordCnt", is(12)))
                .andDo(print());
    }

    @Test
    @DisplayName("인기 검색어가 없으면 결과가 없다고 안내문이 떠야한다")
    void get_top10_keyword_not_exist_test() throws Exception  {
        searchHistoryRepository.deleteAll(); // 기존 db 내용 지우기

        mockMvc.perform(get("/topKeyword"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.code", is(204)))
                .andExpect(jsonPath("$.message", is("검색 내용이 없습니다.")))
                .andDo(print());
    }

}