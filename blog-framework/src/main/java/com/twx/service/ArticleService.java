package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.dto.AddArticleDto;
import com.twx.domain.entity.Article;

/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2024-05-03 13:13:04
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId,Long userId);

    ResponseResult getArticleDetail(Long id,Long currentUserId);

//    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult deleteArticle(Long id);

    ResponseResult updateArticle(AddArticleDto articleDto);

    ResponseResult draftArticleList(Integer pageNum, Integer pageSize, Long userId);

    ResponseResult searchArticle(Integer pageNum, Integer pageSize, String content);

    ResponseResult like(Long articleId, Long userId);

    ResponseResult dislike(Long articleId, Long userId);

    ResponseResult star(Long articleId, Long userId);

    ResponseResult deleteStar(Long articleId, Long userId);

    ResponseResult starList(Integer pageNum, Integer pageSize, Long userId);

    ResponseResult addViewCount(Long articleId);

    ResponseResult isAddPraise(Long currentUserId);

}

