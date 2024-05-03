package com.twx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.entity.Article;
import com.twx.mapper.ArticleMapper;
import com.twx.service.ArticleService;
import org.springframework.stereotype.Service;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2024-05-03 13:13:06
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}

