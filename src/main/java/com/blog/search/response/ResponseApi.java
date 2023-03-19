package com.blog.search.response;

import com.blog.search.model.Blog;
import com.blog.search.model.Meta;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseApi {
    private Meta meta;
    private List<Blog> documents;
}
