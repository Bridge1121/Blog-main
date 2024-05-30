package com.twx.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.twx.domain.entity.Article;
import com.twx.domain.entity.Comment;
import com.twx.mapper.ArticleMapper;
import com.twx.mapper.CommentMapper;
import com.twx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PraiseAndCommentCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id praiseCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Long> praiseCountMap = articles.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {

                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Long>() {

                    @Override
                    public Long apply(Article article) {
                        return article.getPraises();
                    }
                }));
        //存储到redis中
        redisCache.setCacheMap("article:praiseCount",praiseCountMap);
        Map<String, Integer> commentCountMap = articles.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {
                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Integer>() {

                    @Override
                    public Integer apply(Article article) {
                        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(Comment::getArticleId,article.getId());
                        List<Comment> comments = commentMapper.selectList(queryWrapper);
                        return comments.size();
                    }
                }));
        //存储到redis中
        redisCache.setCacheMap("article:commentCount",commentCountMap);
    }
}
