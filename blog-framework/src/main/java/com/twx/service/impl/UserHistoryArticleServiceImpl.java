package com.twx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.UserHistoryArticle;
import com.twx.enums.AppHttpCodeEnum;
import com.twx.mapper.UserHistoryArticleMapper;
import com.twx.service.UserHistoryArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 用户历史浏览记录表(UserHistoryArticle)表服务实现类
 *
 * @author makejava
 * @since 2024-06-05 14:18:40
 */
@Service("userHistoryArticleService")
public class UserHistoryArticleServiceImpl extends ServiceImpl<UserHistoryArticleMapper, UserHistoryArticle> implements UserHistoryArticleService {
    @Autowired
    private UserHistoryArticleService userHistoryArticleService;


    @Override
    public ResponseResult addHistory(Long userId, Long articleId) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDateTime.format(formatter);
        if (!historyExist(userId,articleId, formattedDate)){
            UserHistoryArticle userHistoryArticle = new UserHistoryArticle(articleId,userId,formattedDate);
            save(userHistoryArticle);
        }
        return ResponseResult.okResult();
    }

    public boolean historyExist(Long userId, Long articleId,String date){
        LambdaQueryWrapper<UserHistoryArticle> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserHistoryArticle::getArticleid,articleId);
        queryWrapper.eq(UserHistoryArticle::getUserid,userId);
        queryWrapper.eq(UserHistoryArticle::getTime,date);
        return count(queryWrapper)>0;
    }



}

