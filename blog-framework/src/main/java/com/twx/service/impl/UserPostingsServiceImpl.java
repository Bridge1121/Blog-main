package com.twx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.User;
import com.twx.domain.entity.UserPostings;
import com.twx.domain.entity.UserPraisePostings;
import com.twx.domain.vo.PageVo;
import com.twx.domain.vo.UserPostingsVo;
import com.twx.mapper.UserPostingsMapper;
import com.twx.mapper.UserPraisePostingsMapper;
import com.twx.service.UserPostingsService;
import com.twx.service.UserPraisePostingsService;
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
    @Autowired
    private UserPraisePostingsService userPraisePostingsService;
    @Autowired
    private UserPraisePostingsMapper userPraisePostingsMapper;

    @Override
    public ResponseResult createUserPosting(UserPostings userPostings) {
        save(userPostings);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listByUserId(Integer pageNum, Integer pageSize,Long userId) {
        LambdaQueryWrapper<UserPostings> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserPostings::getDelFlag,0);
        lambdaQueryWrapper.eq(UserPostings::getCreateBy,userId);
        lambdaQueryWrapper.orderByDesc(UserPostings::getCreateTime);//按创建时间降序排序
        List<UserPostings> userpostings = userPostingsService.list(lambdaQueryWrapper);
        //分页查询
        Page<UserPostings> page = new Page<>(pageNum,pageSize);
        page(page, lambdaQueryWrapper);
        List<UserPostings> records = page.getRecords();
        List<UserPostingsVo> userPostingsVos = BeanCopyUtils.copyBeanList(records, UserPostingsVo.class);
        for (UserPostingsVo vo:userPostingsVos){
            User user = userService.getById(vo.getCreateBy());
            vo.setCreateUserName(user.getNickName());
            vo.setAvatar(user.getAvatar());
            LambdaQueryWrapper<UserPraisePostings> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(UserPraisePostings::getUserid,userId);
            queryWrapper1.eq(UserPraisePostings::getPostingid,vo.getId());
            List<UserPraisePostings> list = userPraisePostingsService.list(queryWrapper1);
            if (list.size()!=0){
                vo.setPraise(true);//当前用户已经对该动态点赞过
            }else {
                vo.setPraise(false);
            }
        }
        return ResponseResult.okResult(new PageVo(userPostingsVos,page.getTotal()));
    }

    @Override
    public ResponseResult postingslist(Integer pageNum, Integer pageSize,Long currentUserId) {
        //查询条件
        LambdaQueryWrapper<UserPostings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPostings::getDelFlag,0);
        //对istop进行降序
        queryWrapper.orderByDesc(UserPostings::getCreateTime);
        //分页查询
        Page<UserPostings> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        List<UserPostings> records = page.getRecords();
        List<UserPostingsVo> userPostingsVos = BeanCopyUtils.copyBeanList(records, UserPostingsVo.class);
        for (UserPostingsVo vo:userPostingsVos){
            User user = userService.getById(vo.getCreateBy());
            vo.setCreateUserName(user.getNickName());
            vo.setAvatar(user.getAvatar());
            LambdaQueryWrapper<UserPraisePostings> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(UserPraisePostings::getUserid,currentUserId);
            queryWrapper1.eq(UserPraisePostings::getPostingid,vo.getId());
            List<UserPraisePostings> list = userPraisePostingsService.list(queryWrapper1);
            if (list.size()!=0){
                vo.setPraise(true);//当前用户已经对该动态点赞过
            }else {
                vo.setPraise(false);
            }
        }
        return ResponseResult.okResult(new PageVo(userPostingsVos,page.getTotal()));
    }

    @Override
    public ResponseResult addPrize(Long currentUserId, Long postingId) {
        UserPostings userPostings = getById(postingId);
        LambdaUpdateWrapper<UserPostings> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserPostings::getId,postingId);
        updateWrapper.set(UserPostings::getPraises,userPostings.getPraises()+1);
        update(null,updateWrapper);
        UserPraisePostings userPraisePostings = new UserPraisePostings(currentUserId,postingId);
        userPraisePostingsService.save(userPraisePostings);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deletePrize(Long currentUserId, Long postingId) {
        LambdaQueryWrapper<UserPraisePostings> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPraisePostings::getPostingid,postingId);
        queryWrapper.eq(UserPraisePostings::getUserid,currentUserId);
        userPraisePostingsMapper.delete(queryWrapper);
        UserPostings userPostings = getById(postingId);
        LambdaUpdateWrapper<UserPostings> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserPostings::getId,postingId);
        updateWrapper.set(UserPostings::getPraises,userPostings.getPraises()-1);
        update(null,updateWrapper);
        return ResponseResult.okResult();
    }
}

