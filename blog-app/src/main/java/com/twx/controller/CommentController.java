package com.twx.controller;

import com.twx.domain.ResponseResult;
import com.twx.domain.entity.Comment;
import com.twx.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Api(tags = "评论",description = "评论相关接口")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/commentList")
    @ApiOperation(value = "获取评论列表",notes = "获取当前文章或动态所有评论")
    public ResponseResult commentList(Long currentUserId,Long articleId,Integer pageNum,Integer pageSize,boolean isArticle){
        return commentService.commentList(currentUserId,articleId,pageNum,pageSize,isArticle);
    }

    @GetMapping("/replyList")
    @ApiOperation(value = "获取当前评论的子评论列表",notes = "获取当前评论的子评论")
    public ResponseResult replyList(Long currentUserId,Long commentId,Integer pageNum,Integer pageSize){
        return commentService.replyList(currentUserId,commentId,pageNum,pageSize);
    }

    @PostMapping
    @ApiOperation(value = "发表评论",notes = "对当前文章发表评论")
    public ResponseResult addComment(@RequestBody Comment comment){
        return commentService.addComment(comment);
    }

    @GetMapping("/addPrize")
    public ResponseResult addPrize(Long currentUserId,Long commentId){
        return commentService.addPrize(currentUserId,commentId);
    }

    @DeleteMapping("/deletePrize")
    public ResponseResult deletePrize(Long currentUserId,Long commentId){
        return commentService.deletePrize(currentUserId,commentId);
    }

}
