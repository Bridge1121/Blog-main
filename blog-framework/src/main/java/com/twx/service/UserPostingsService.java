package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.UserPostings;


/**
 * (UserPostings)表服务接口
 *
 * @author makejava
 * @since 2024-05-26 12:43:02
 */
public interface UserPostingsService extends IService<UserPostings> {

    ResponseResult createUserPosting(UserPostings userPostings);

    ResponseResult listByUserId(Integer pageNum, Integer pageSize,Long userId);

    ResponseResult postingslist(Integer pageNum, Integer pageSize,Long currentUserId);

    ResponseResult addPrize(Long currentUserId, Long postingId);

    ResponseResult deletePrize(Long currentUserId, Long postingId);
}
