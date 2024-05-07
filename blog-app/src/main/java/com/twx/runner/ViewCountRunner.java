package com.twx.runner;

import com.twx.domain.entity.Article;
import com.twx.mapper.ArticleMapper;
import com.twx.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息 id viewcount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(new Function<Article, String>() {

                    @Override
                    public String apply(Article article) {
                        return article.getId().toString();
                    }
                }, new Function<Article, Integer>() {

                    @Override
                    public Integer apply(Article article) {
                        return article.getViewCount().intValue();
                    }
                }));
        //存储到redis中
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}
