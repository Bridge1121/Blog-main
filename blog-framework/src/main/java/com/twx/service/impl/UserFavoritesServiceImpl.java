package com.twx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.entity.UserFavorites;
import com.twx.mapper.UserFavoritesMapper;
import com.twx.service.UserFavoritesService;
import org.springframework.stereotype.Service;

/**
 * (UserFavorites)表服务实现类
 *
 * @author makejava
 * @since 2024-05-26 08:26:35
 */
@Service("userFavoritesService")
public class UserFavoritesServiceImpl extends ServiceImpl<UserFavoritesMapper, UserFavorites> implements UserFavoritesService {

}

