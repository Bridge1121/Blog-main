package com.twx.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.constants.SystemConstants;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.Comment;
import com.twx.domain.vo.*;
import com.twx.enums.AppHttpCodeEnum;
import com.twx.exception.SystemException;
import com.twx.mapper.CommentMapper;
import com.twx.service.CommentService;
import com.twx.service.UserService;
import com.twx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-05-04 12:41:02
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getRootId, -1);

        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        for (CommentVo commentVo : commentVoList) {
            List<CommentReplyVo> children = getChildren(commentVo.getId());
            commentVo.setReplies(children);
            commentVo.setCurrentPage(pageNum);
            commentVo.setPageSize(pageSize);
            commentVo.setTotalPages((int) page.getPages());
            commentVo.setTotalDataSize((int) page.getTotal());
            commentVo.setNextPage(page.hasNext() ? (int) (page.getCurrent() + 1) : -1);
            commentVo.setPrefPage(page.hasPrevious() ? (int) (page.getCurrent() - 1) : -1);

        }
        return ResponseResult.okResult(new PagerEnableVo(commentVoList,pageNum,pageSize,(int) page.getPages(),(int) page.getTotal(),page.hasNext() ? (int) (page.getCurrent() + 1) : -1,page.hasPrevious() ? (int) (page.getCurrent() - 1) : -1));

    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentReplyVo> getChildren(Long id) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);

        List<CommentReplyVo> commentVos = toCommentReplyVoList(comments);
        return commentVos;
    }

    private List<CommentReplyVo> toCommentReplyVoList(List<Comment> list){
        List<CommentReplyVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentReplyVo.class);
        //遍历vo集合
        for (CommentReplyVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUserName(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUserName(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}

