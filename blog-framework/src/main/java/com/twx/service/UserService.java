package com.twx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-05-04 13:08:30
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getAvatar(Long userId);
}
