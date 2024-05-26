package com.twx.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserFavorites)表实体类
 *
 * @author makejava
 * @since 2024-05-26 08:39:50
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@TableName("blog_user_favorites")
public class UserFavorites  {
    //当前登录的用户id@TableId
    private Long userid;
    //收藏的文章id@TableId
    private Long articleid;


    public UserFavorites(Long userid, Long articleid) {
        this.userid = userid;
        this.articleid = articleid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getArticleid() {
        return articleid;
    }

    public void setArticleid(Long articleid) {
        this.articleid = articleid;
    }
}
