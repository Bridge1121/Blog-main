package com.twx.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.constants.SystemConstants;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.Article;
import com.twx.domain.entity.Comment;
import com.twx.domain.entity.UserPostings;
import com.twx.domain.entity.UserPraiseComment;
import com.twx.domain.vo.*;
import com.twx.enums.AppHttpCodeEnum;
import com.twx.exception.SystemException;
import com.twx.mapper.CommentMapper;
import com.twx.mapper.UserPostingsMapper;
import com.twx.mapper.UserPraiseCommentMapper;
import com.twx.service.ArticleService;
import com.twx.service.CommentService;
import com.twx.service.UserPraiseCommentService;
import com.twx.service.UserService;
import com.twx.utils.BeanCopyUtils;
import com.twx.utils.RedisCache;
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

    @Autowired
    private UserPraiseCommentService userPraiseCommentService;

    @Autowired
    private UserPraiseCommentMapper userPraiseCommentMapper;

    @Autowired
    private UserPostingsMapper userPostingsMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisCache redisCache;




    @Override
    public ResponseResult commentList(Long currentUserId,Long articleId, Integer pageNum, Integer pageSize,boolean isArticle) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getRootId, -1);
        if (isArticle){//查询文章评论
            queryWrapper.eq(Comment::getType,"0");

        }else{//查询动态评论
            queryWrapper.eq(Comment::getType,"1");
        }

        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        for (CommentVo commentVo : commentVoList) {
            commentVo.setPraise(isPraise(commentVo.getId(),currentUserId));//currentUserId是为了获取当前用户点赞过哪些评论
            PagerRepliesEnableVo children = getChildren(1,2,commentVo.getId(),currentUserId);//第一次找当前评论的子评论时只找第一页的前两条
            commentVo.setReplies(children.getReplies());
            commentVo.setCurrentPage(children.getCurrentPage());
            commentVo.setPageSize(children.getPageSize());
            commentVo.setTotalPages(children.getTotalPages());
            commentVo.setTotalDataSize(children.getTotalDataSize());
            commentVo.setNextPage(children.getCurrentPage()<children.getTotalPages() ? (int) (page.getCurrent() + 1) : -1);
            commentVo.setPrefPage(children.getCurrentPage()>1 ? (int) (page.getCurrent() - 1) : -1);

        }
        return ResponseResult.okResult(new PagerEnableVo(commentVoList,pageNum,pageSize,(int) page.getPages(),(int) page.getTotal(),page.hasNext() ? (int) (page.getCurrent() + 1) : -1,page.hasPrevious() ? (int) (page.getCurrent() - 1) : -1));

    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //评论内容不能为空
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        if (comment.getType().equals("1")) {//添加的是动态评论
            UserPostings userPostings = userPostingsMapper.selectById(comment.getArticleId());
            LambdaUpdateWrapper<UserPostings> userPostingsLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            userPostingsLambdaUpdateWrapper.set(UserPostings::getComments,userPostings.getComments()+1);
            userPostingsLambdaUpdateWrapper.eq(UserPostings::getId,userPostings.getId());
            userPostingsMapper.update(null,userPostingsLambdaUpdateWrapper);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult replyList(Long currentUserId,Long commentId, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,commentId);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<CommentReplyVo> commentVoList = toCommentReplyVoList(page.getRecords());
        for (CommentReplyVo commentReplyVo:commentVoList){
            commentReplyVo.setPrize(isPraise(commentReplyVo.getId(),currentUserId));
        }

//        List<CommentReplyVo> commentVos = toCommentReplyVoList(commentVoList);
        return ResponseResult.okResult(new PagerRepliesEnableVo(commentVoList,pageNum,pageSize,(int) page.getPages(),(int) page.getTotal(),page.hasNext() ? (int) (page.getCurrent() + 1) : -1,page.hasPrevious() ? (int) (page.getCurrent() - 1) : -1));
    }

    @Override
    public ResponseResult addPrize(Long currentUserId,Long commentId) {
        Comment comment = getById(commentId);
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getId,commentId);
        updateWrapper.set(Comment::getPrizes,comment.getPrizes()+1);
        update(null,updateWrapper);
        UserPraiseComment userPraiseComment = new UserPraiseComment(currentUserId,commentId);
        userPraiseCommentService.save(userPraiseComment);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deletePrize(Long currentUserId, Long commentId) {
        LambdaQueryWrapper<UserPraiseComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPraiseComment::getCommentid,commentId);
        queryWrapper.eq(UserPraiseComment::getUserid,currentUserId);
        userPraiseCommentMapper.delete(queryWrapper);
        Comment comment = getById(commentId);
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getId,commentId);
        updateWrapper.set(Comment::getPrizes,comment.getPrizes()-1);
        update(null,updateWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult isAddComment(Long currentUserId) {
        //查询当前登录用户所有的文章信息
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy,currentUserId);
        List<Article> articles = articleService.list(queryWrapper);
        //遍历每篇文章查询所有评论信息
        for (Article artice:articles) {
            //判断是否新增评论
            LambdaQueryWrapper<Comment> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Comment::getArticleId,artice.getId());
            List<Comment> comments = commentMapper.selectList(queryWrapper1);
            Integer lasetCommentCounts = redisCache.getCacheMapValue("article:commentCount", artice.getId().toString());

            if (comments.size()>lasetCommentCounts){
                //如果有，更新rediscache中的对应文章的评论数量
                redisCache.setCacheMapValue("article:commentCount", artice.getId().toString(),comments.size());
                return ResponseResult.okResult(new AddPraiseVo(true,artice.getId()));
            }else if (comments.size()<lasetCommentCounts){
                redisCache.setCacheMapValue("article:commentCount", artice.getId().toString(),comments.size());
            }

        }

        return ResponseResult.okResult(new AddPraiseVo(false,null));
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private PagerRepliesEnableVo getChildren(int pageNum,int pageSize,Long id,Long currentUserId) {

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<CommentReplyVo> commentVoList = toCommentReplyVoList(page.getRecords());
        for(int i =0;i<commentVoList.size();i++){//判断每条回复是否被当前用户点赞过
            commentVoList.get(i).setPrize(isPraise(commentVoList.get(i).getId(),currentUserId));
        }

//        List<CommentReplyVo> commentVos = toCommentReplyVoList(commentVoList);
        return new PagerRepliesEnableVo(commentVoList,pageNum,pageSize,(int) page.getPages(),(int) page.getTotal(),page.hasNext() ? (int) (page.getCurrent() + 1) : -1,page.hasPrevious() ? (int) (page.getCurrent() - 1) : -1);
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

    //判断当前评论是否被当前登录用户点赞
    private boolean isPraise(Long commentId,Long currentUserId){
        LambdaQueryWrapper<UserPraiseComment> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(UserPraiseComment::getCommentid,commentId);
        queryWrapper1.eq(UserPraiseComment::getUserid,currentUserId);
        List<UserPraiseComment> userPraiseComments = userPraiseCommentService.list(queryWrapper1);
        if (userPraiseComments.size()==0){
            return false;
        }
        return true;
    }
}

