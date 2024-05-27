package com.twx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.entity.UserPraisePostings;
import com.twx.mapper.UserPraisePostingsMapper;
import com.twx.service.UserPraisePostingsService;
import org.springframework.stereotype.Service;

/**
 * (UserPraisePostings)表服务实现类
 *
 * @author makejava
 * @since 2024-05-27 07:53:13
 */
@Service("userPraisePostingsService")
public class UserPraisePostingsServiceImpl extends ServiceImpl<UserPraisePostingsMapper, UserPraisePostings> implements UserPraisePostingsService {

}

