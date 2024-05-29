package com.twx.controller;

import com.twx.annotation.SystemLog;
import com.twx.domain.ResponseResult;
import com.twx.domain.entity.User;
import com.twx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户",description = "用户相关接口")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation(value = "用户信息获取",notes = "获取用户当前个人信息")
    public ResponseResult userInfo(){
        return userService.userInfo();
    }


    @PutMapping("/updateUserInfo")
    @SystemLog(businessName = "更新用户信息")
    @ApiOperation(value = "更新用户信息",notes = "更新用户当前个人信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册",notes = "注册新用户")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/getAvatar")
    public ResponseResult getAvatar(Long userId){
        return userService.getAvatar(userId);
    }

    @GetMapping("/follow")
    public ResponseResult follow(Long userId,Long followId){
        return userService.follow(userId,followId);
    }

    @DeleteMapping("/cancelFollow")
    public ResponseResult cancelFollow(Long userId,Long followId){
        return userService.cancelFollow(userId,followId);
    }

    @GetMapping("/followerList")
    @ApiOperation(value = "分页查询当前用户关注列表",notes = "查询当前用户关注列表")
    public ResponseResult followerList(Integer pageNum,Integer pageSize,Long userId){
        return userService.followerList(pageNum,pageSize,userId);
    }

}
