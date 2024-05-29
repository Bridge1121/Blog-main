package com.twx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.constants.SystemConstants;
import com.twx.domain.ResponseResult;
import com.twx.domain.dto.AddArticleDto;
import com.twx.domain.entity.*;
import com.twx.domain.vo.*;
import com.twx.enums.AppHttpCodeEnum;
import com.twx.exception.SystemException;
import com.twx.mapper.ArticleMapper;
import com.twx.mapper.UserFavoritesMapper;
import com.twx.mapper.UserLikeArticleMapper;
import com.twx.service.*;
import com.twx.utils.BeanCopyUtils;
import com.twx.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.QueryEval;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2024-05-03 13:13:06
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserLikeArticleService userLikeArticleService;
    @Autowired
    private UserLikeArticleMapper userLikeArticleMapper;

    @Autowired
    private UserFavoritesService userFavoritesService;
    @Autowired
    private UserFavoritesMapper userFavoritesMapper;

    @Autowired
    private UserFollowersService userFollowersService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章，封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照点赞收藏排序
        queryWrapper.orderByDesc(Article::getPraises);
        queryWrapper.orderByDesc(Article::getStars);
        //最多查询10条
        Page<Article> page = new Page(1,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        List<HotArticleVo> articleVos = new ArrayList<>();
        //bean拷贝
//        for (Article article:articles){
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article,vo);
//            articleVos.add(vo);
//        }
        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles,HotArticleVo.class);

        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId,Long userId) {

        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId就要和查询时传入的相同
        queryWrapper.eq(Objects.nonNull(userId),Article::getCreateBy,userId);
        //如果有categoryId就要和查询时传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId) && categoryId>0,Article::getCategoryId,categoryId);
        //状态是正式发布的
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //对创建时间进行降序
        queryWrapper.orderByDesc(Article::getCreateTime);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        //查询categoryName
        List<Article> articles = page.getRecords();
//        articles.stream()//流式编程
//                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
//                .collect(Collectors.toList());
        //用articleId查询articleName进行设置
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult starList(Integer pageNum, Integer pageSize, Long userId) {
        //先查用户收藏关联表，得到收藏的文章id
        LambdaQueryWrapper<UserFavorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFavorites::getUserid,userId);
        Page<UserFavorites> page = new Page<>(pageNum,pageSize);
        userFavoritesService.page(page,queryWrapper);
        List<UserFavorites> userFavorites = page.getRecords();
        List<Article> articles = new ArrayList<>();
        for (UserFavorites userFavorites1:userFavorites){
            Article article = getById(userFavorites1.getArticleid());
            articles.add(article);
        }
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        return ResponseResult.okResult(new PageVo(articleListVos,page.getTotal()));
    }

    @Override
    public ResponseResult getArticleDetail(Long id,Long currentUserId) {//查询文章具体内容
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        if (viewCount!=null){
            article.setViewCount(viewCount.longValue());
        }else{
            article.setViewCount(new Long(0));
        }

        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //去查用户点赞关联表看是否点赞过
        LambdaQueryWrapper<UserLikeArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLikeArticle::getArticleid,id);
        queryWrapper.eq(UserLikeArticle::getUserid,currentUserId);
        List<UserLikeArticle> list = userLikeArticleService.list();
        if (list.size()!=0){
            articleDetailVo.setPraise(true);
        }else {
            articleDetailVo.setPraise(false);
        }
        //查用户收藏关联表看是否收藏过
        LambdaQueryWrapper<UserFavorites> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(UserFavorites::getArticleid,id);
        queryWrapper1.eq(UserFavorites::getUserid,currentUserId);
        List<UserFavorites> list1 = userFavoritesService.list(queryWrapper1);
        if (list1.size()!=0){
            articleDetailVo.setStar(true);
        }else {
            articleDetailVo.setStar(false);
        }
        //根据作者id查具体信息
        User author = userService.getById(article.getCreateBy());
        UserInfoVo authorInfo = BeanCopyUtils.copyBean(author, UserInfoVo.class);
        LambdaQueryWrapper<UserFollowers> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(UserFollowers::getFollowerid,article.getCreateBy());
        queryWrapper2.eq(UserFollowers::getUserid,currentUserId);
        List<UserFollowers> list2 = userFollowersService.list(queryWrapper2);
        if (list2.size()!=0){
            authorInfo.setFollow(true);
        }else{
            authorInfo.setFollow(false);
        }
        articleDetailVo.setAuthor(authorInfo);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category!=null){//避免空指针
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto,Article.class);
        save(article);
//        List<ArticleTag> articleTags = articleDto.getTags().stream().map(tagId -> new ArticleTag(article.getId(), tagId)).collect(Collectors.toList());
//        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        articleService.removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateArticle(AddArticleDto articleDto) {
        Article article = articleService.getById(articleDto.getId());
        if (article.getDelFlag()==1){
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOT_EXISTS);
        }
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getCategoryId,articleDto.getCategoryId());
        updateWrapper.set(Article::getTitle,articleDto.getTitle());
        updateWrapper.set(Article::getThumbnail,articleDto.getThumbnail());
        updateWrapper.set(Article::getContent,articleDto.getContent());
        updateWrapper.eq(Article::getId,articleDto.getId());
        articleMapper.update(null,updateWrapper);
//        Article newArticle = BeanCopyUtils.copyBean(articleDto,Article.class);
//        articleMapper.updateById(newArticle);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult draftArticleList(Integer pageNum, Integer pageSize, Long userId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getCreateBy,userId);
        //状态是未发布的
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_UNNORMAL);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        //查询categoryName
        List<Article> articles = page.getRecords();
        //用articleId查询articleName进行设置
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);


        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //模糊查询文章
    @Override
    public ResponseResult searchArticle(Integer pageNum, Integer pageSize, String content) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Article::getTitle,content).or().like(Article::getSummary,content).or().like(Article::getContent,content);
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        //查询categoryName
        List<Article> articles = page.getRecords();
        //用articleId查询articleName进行设置
        for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult like(Long articleId, Long userId) {
        //更新文章表，点赞数量+1
        Article article = getById(articleId);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,articleId);
        updateWrapper.set(Article::getPraises,article.getPraises()+1);
        update(null,updateWrapper);
        //更新用户点赞关联表，添加记录
        UserLikeArticle userLikeArticle = new UserLikeArticle(userId,articleId);
        userLikeArticleService.save(userLikeArticle);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult dislike(Long articleId, Long userId) {
        //更新文章表，点赞数量-1
        Article article = getById(articleId);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,articleId);
        updateWrapper.set(Article::getPraises,article.getPraises()-1);
        update(null,updateWrapper);
        //更新用户点赞关联表，删除记录
        LambdaUpdateWrapper<UserLikeArticle> querywrapper = new LambdaUpdateWrapper<>();
        querywrapper.eq(UserLikeArticle::getArticleid,articleId);
        querywrapper.eq(UserLikeArticle::getUserid,userId);
        userLikeArticleMapper.delete(querywrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult star(Long articleId, Long userId) {
        //更新文章表，收藏+1
        Article article = getById(articleId);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,articleId);
        updateWrapper.set(Article::getStars,article.getStars()+1);
        update(null,updateWrapper);
        //更新用户收藏表，新增记录
        UserFavorites userFavorites = new UserFavorites(userId,articleId);
        userFavoritesService.save(userFavorites);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteStar(Long articleId, Long userId) {
        //更新文章表，收藏-1
        Article article = getById(articleId);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,articleId);
        updateWrapper.set(Article::getStars,article.getStars()-1);
        update(null,updateWrapper);
        //更新用户收藏关联表，删除记录
        LambdaUpdateWrapper<UserFavorites> querywrapper = new LambdaUpdateWrapper<>();
        querywrapper.eq(UserFavorites::getArticleid,articleId);
        querywrapper.eq(UserFavorites::getUserid,userId);
        userFavoritesMapper.delete(querywrapper);
        return ResponseResult.okResult();
    }




}

