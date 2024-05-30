package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2024-05-04 12:41:00
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(Long currentUserId,Long articleId, Integer pageNum, Integer pageSize,boolean isArticle);

    ResponseResult addComment(Comment comment);

    ResponseResult replyList(Long currentUserId,Long commentId, Integer pageNum, Integer pageSize);

    ResponseResult addPrize(Long currentUserId,Long commentId);

    ResponseResult deletePrize(Long currentUserId, Long commentId);

    ResponseResult isAddComment(Long currentUserId);
}
