package com.twx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogUserLoginVo {

    private String token;
    private UserInfoVo userInfo;

    public BlogUserLoginVo(String token, UserInfoVo userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }
}