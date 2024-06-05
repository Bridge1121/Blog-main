package com.twx.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户历史浏览记录表(UserHistoryArticle)表实体类
 *
 * @author makejava
 * @since 2024-06-05 14:18:40
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@TableName("blog_user_history_article")
public class UserHistoryArticle {
//    @TableId
    private Long articleid;

    
    private Long userid;

    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getArticleid() {
        return articleid;
    }

    public void setArticleid(Long articleid) {
        this.articleid = articleid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public UserHistoryArticle(Long articleid, Long userid, String time) {
        this.articleid = articleid;
        this.userid = userid;
        this.time = time;
    }
}
