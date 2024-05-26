package com.twx.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserPraiseComment)表实体类
 *
 * @author makejava
 * @since 2024-05-26 08:46:23
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@TableName("blog_user_praise_comment")
public class UserPraiseComment {
    //当前登录的用户id@TableId
    private Long userid;
    //当前用户点赞过的评论id@TableId
    private Long commentid;

    public UserPraiseComment(Long userid, Long commentid) {
        this.userid = userid;
        this.commentid = commentid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getCommentid() {
        return commentid;
    }

    public void setCommentid(Long commentid) {
        this.commentid = commentid;
    }
}
