package com.twx.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserLikeArticle)表实体类
 *
 * @author makejava
 * @since 2024-05-27 14:06:38
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@TableName("blog_user_like_article")
public class UserLikeArticle {
//    @TableId
    private Long userid;
//    @TableId
    private Long articleid;

    public UserLikeArticle(Long userid, Long articleid) {
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
