package com.twx.controller;

import com.twx.domain.ResponseResult;
import com.twx.service.SearchContentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Api(tags = "搜索内容",description = "搜索内容相关接口")
public class SearchContentController {

    @Autowired
    private SearchContentService searchContentService;

    @GetMapping("/getHotList")
    public ResponseResult getHotList(Integer pageNum,Integer pageSize){
        return searchContentService.getHotList(pageNum,pageSize);
    }
}
