package com.twx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.entity.UserPraiseComment;
import com.twx.mapper.UserPraiseCommentMapper;
import com.twx.service.UserPraiseCommentService;
import org.springframework.stereotype.Service;

/**
 * (UserPraiseComment)表服务实现类
 *
 * @author makejava
 * @since 2024-05-26 08:46:27
 */
@Service("userPraiseCommentService")
public class UserPraiseCommentServiceImpl extends ServiceImpl<UserPraiseCommentMapper, UserPraiseComment> implements UserPraiseCommentService {

}

