package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.UserHistoryArticle;


/**
 * 用户历史浏览记录表(UserHistoryArticle)表服务接口
 *
 * @author makejava
 * @since 2024-06-05 14:18:40
 */
public interface UserHistoryArticleService extends IService<UserHistoryArticle> {

    ResponseResult addHistory(Long userId, Long articleId);

}
