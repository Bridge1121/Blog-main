package com.twx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.entity.UserFollowers;
import com.twx.mapper.UserFollowersMapper;
import com.twx.service.UserFollowersService;
import org.springframework.stereotype.Service;

/**
 * (UserFollowers)表服务实现类
 *
 * @author makejava
 * @since 2024-05-26 08:28:03
 */
@Service("userFollowersService")
public class UserFollowersServiceImpl extends ServiceImpl<UserFollowersMapper, UserFollowers> implements UserFollowersService {

}

