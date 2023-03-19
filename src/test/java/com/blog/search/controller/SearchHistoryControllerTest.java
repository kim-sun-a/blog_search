package com.blog.search.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SearchHistoryControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

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

}