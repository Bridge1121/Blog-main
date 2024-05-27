package com.twx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.User;
import com.twx.domain.entity.UserFollowers;
import com.twx.domain.vo.PageVo;
import com.twx.domain.vo.UserInfoVo;
import com.twx.enums.AppHttpCodeEnum;
import com.twx.exception.SystemException;
import com.twx.mapper.UserFollowersMapper;
import com.twx.mapper.UserMapper;
import com.twx.service.UserFollowersService;
import com.twx.service.UserService;
import com.twx.utils.BeanCopyUtils;
import com.twx.utils.SecurityUtils;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-05-04 13:08:32
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserFollowersService userFollowersService;
    @Autowired
    private UserFollowersMapper userFollowersMapper;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        //更新局部字段
        LambdaUpdateWrapper<User> userUpdateWrapper = new LambdaUpdateWrapper<>();
        userUpdateWrapper.set(User::getNickName, user.getNickName());
        userUpdateWrapper.set(User::getAvatar, user.getAvatar());
        userUpdateWrapper.set(User::getSex, user.getSex());
        userUpdateWrapper.set(User::getEmail, user.getEmail());
//        if (!StringUtils.isEmpty(user.getPassword())) {
//            userUpdateWrapper.set(User::getPassword, passwordEncoder.encode(user.getPassword()));
//        }
        userUpdateWrapper.eq(User::getId, user.getId());
        userMapper.update(null, userUpdateWrapper);
        return ResponseResult.okResult();
    }



    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        if(!StringUtils.hasText(user.getAvatar())){
            //上传默认头像
            user.setAvatar("http://scy727740.hd-bkt.clouddn.com/2024/05/11/5ea2264b4dc34664a7c65b69af1901c0.png");
        }
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getAvatar(Long userId) {
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getId,userId);
        User user = getById(userId);
        return ResponseResult.okResult(user.getAvatar());
    }

    @Override
    public ResponseResult follow(Long userId, Long followId) {
        //更新用户表中followId对应用户的fans数量和userId对应用户的follower数量
        User user = getById(userId);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,userId);
        updateWrapper.set(User::getFollowers,user.getFollowers()+1);
        update(null,updateWrapper);
        User follower = getById(followId);
        LambdaUpdateWrapper<User> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1.eq(User::getId,followId);
        updateWrapper1.set(User::getFans,follower.getFans()+1);
        update(null,updateWrapper1);
        //更新用户关注关联表，新增记录
        UserFollowers userFollowers = new UserFollowers(followId,userId);
        userFollowersService.save(userFollowers);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult cancelFollow(Long userId, Long followId) {
        //更新用户表中followId对应用户的fans数量和userId对应用户的follower数量
        User user = getById(userId);
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,userId);
        updateWrapper.set(User::getFollowers,user.getFollowers()-1);
        update(null,updateWrapper);
        User follower = getById(followId);
        LambdaUpdateWrapper<User> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1.eq(User::getId,followId);
        updateWrapper1.set(User::getFans,follower.getFans()-1);
        update(null,updateWrapper1);
        //更新用户关注关联表，删除记录
        LambdaQueryWrapper<UserFollowers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollowers::getFollowerid,followId);
        queryWrapper.eq(UserFollowers::getUserid,userId);
        userFollowersMapper.delete(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult followerList(Integer pageNum, Integer pageSize, Long userId) {
        //查询用户关注关联表得到关注的用户id
        LambdaQueryWrapper<UserFollowers> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFollowers::getUserid,userId);
        Page<UserFollowers> page = new Page<>();
        userFollowersService.page(page, queryWrapper);
        List<UserFollowers> userFollowers = page.getRecords();
        List<User> users = new ArrayList<>();
        for (UserFollowers userFollowers1:userFollowers){
            User user = getById(userFollowers1.getFollowerid());
            users.add(user);
        }
        List<UserInfoVo> userInfoVos = BeanCopyUtils.copyBeanList(users, UserInfoVo.class);
        userInfoVos.forEach(userInfoVo -> userInfoVo.setFollow(true));
        return ResponseResult.okResult(new PageVo(userInfoVos,page.getTotal()));
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);

        return count(queryWrapper)>0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);

        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);

        return count(queryWrapper)>0;
    }
}

