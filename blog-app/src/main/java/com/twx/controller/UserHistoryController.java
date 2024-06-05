package com.twx.controller;


import com.twx.domain.ResponseResult;
import com.twx.service.UserHistoryArticleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/history")
@Api(tags = "历史浏览",description = "历史浏览相关接口")
public class UserHistoryController {

    @Autowired
    private UserHistoryArticleService userHistoryArticleService;

    @GetMapping("/addHistory")
    public ResponseResult addHistory(Long userId,Long articleId){
        return userHistoryArticleService.addHistory(userId,articleId);
    }


}
