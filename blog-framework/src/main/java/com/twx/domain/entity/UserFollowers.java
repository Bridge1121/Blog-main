package com.twx.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserFollowers)表实体类
 *
 * @author makejava
 * @since 2024-05-26 08:43:12
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@TableName("blog_user_followers")
public class UserFollowers  {
    //关注的用户id@TableId
    private Long followerid;
    //当前登录当的用户id@TableId
    private Long userid;

    public UserFollowers(Long followerid, Long userid) {
        this.followerid = followerid;
        this.userid = userid;
    }

    public Long getFollowerid() {
        return followerid;
    }

    public void setFollowerid(Long followerid) {
        this.followerid = followerid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
