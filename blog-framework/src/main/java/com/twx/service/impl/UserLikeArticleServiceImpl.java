package com.twx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.entity.UserLikeArticle;
import com.twx.mapper.UserLikeArticleMapper;
import com.twx.service.UserLikeArticleService;
import org.springframework.stereotype.Service;

/**
 * (UserLikeArticle)表服务实现类
 *
 * @author makejava
 * @since 2024-05-27 14:06:42
 */
@Service("userLikeArticleService")
public class UserLikeArticleServiceImpl extends ServiceImpl<UserLikeArticleMapper, UserLikeArticle> implements UserLikeArticleService {

}

