package com.twx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.User;
import com.twx.domain.entity.UserPostings;
import com.twx.domain.vo.PageVo;
import com.twx.domain.vo.UserPostingsVo;
import com.twx.mapper.UserPostingsMapper;
import com.twx.service.UserPostingsService;
import com.twx.service.UserService;
import com.twx.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.callback.LanguageCallback;
import java.util.List;

/**
 * (UserPostings)表服务实现类
 *
 * @author makejava
 * @since 2024-05-26 12:43:04
 */
@Service("userPostingsService")
public class UserPostingsServiceImpl extends ServiceImpl<UserPostingsMapper, UserPostings> implements UserPostingsService {
    @Autowired
    private UserPostingsService userPostingsService;
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult createUserPosting(UserPostings userPostings) {
        save(userPostings);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listByUserId(Long userId) {
        LambdaQueryWrapper<UserPostings> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserPostings::getCreateBy,userId);
        lambdaQueryWrapper.orderByDesc(UserPostings::getPraises);//按点赞数降序排序
        List<UserPostings> userpostings = userPostingsService.list(lambdaQueryWrapper);
        return ResponseResult.okResult(userpostings);
    }

    @Override
    public ResponseResult postingslist(Integer pageNum, Integer pageSize) {
        //查询条件
        LambdaQueryWrapper<UserPostings> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId就要和查询时传入的相同
        queryWrapper.eq(UserPostings::getDelFlag,0);
        //对istop进行降序
        queryWrapper.orderByDesc(UserPostings::getPraises);
        //分页查询
        Page<UserPostings> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        List<UserPostings> records = page.getRecords();
        List<UserPostingsVo> userPostingsVos = BeanCopyUtils.copyBeanList(records, UserPostingsVo.class);
        for (UserPostingsVo vo:userPostingsVos){
            User user = userService.getById(vo.getCreateBy());
            vo.setCreateUserName(user.getNickName());
            vo.setAvatar(user.getAvatar());
        }
        return ResponseResult.okResult(new PageVo(userPostingsVos,page.getTotal()));
    }
}

