package com.twx.service;

import com.twx.domain.ResponseResult;
import com.twx.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
