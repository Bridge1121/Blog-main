package com.twx.controller;

import com.twx.domain.ResponseResult;
import com.twx.domain.entity.UserPostings;
import com.twx.service.UserPostingsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postings")
@Api(tags = "动态",description = "用户动态相关接口")
public class UserPostingsController {

    @Autowired
    private UserPostingsService userPostingsService;

    @PostMapping("/create")
    public ResponseResult createUserPosting(@RequestBody UserPostings userPostings){
        return userPostingsService.createUserPosting(userPostings);
    }

    @GetMapping("/list/{id}")
    public ResponseResult listByUserId(@PathVariable("id") Long userId){
        return userPostingsService.listByUserId(userId);
    }

    @GetMapping("/list")
    public ResponseResult postingslist(Integer pageNum,Integer pageSize,Long currentUserId){
        return userPostingsService.postingslist(pageNum,pageSize,currentUserId);
    }

    @GetMapping("/addPrize")
    public ResponseResult addPrize(Long currentUserId,Long postingId){
        return userPostingsService.addPrize(currentUserId,postingId);
    }

    @DeleteMapping("/deletePrize")
    public ResponseResult deletePrize(Long currentUserId,Long postingId){
        return userPostingsService.deletePrize(currentUserId,postingId);
    }
}
