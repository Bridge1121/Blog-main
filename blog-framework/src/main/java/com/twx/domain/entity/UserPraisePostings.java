package com.twx.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserPraisePostings)表实体类
 *
 * @author makejava
 * @since 2024-05-27 07:53:09
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@TableName("blog_user_praise_postings")
public class UserPraisePostings {
//    @TableId
    private Long userid;
//    @TableId
    private Long postingid;

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getPostingid() {
        return postingid;
    }

    public void setPostingid(Long postingid) {
        this.postingid = postingid;
    }

    public UserPraisePostings(Long userid, Long postingid) {
        this.userid = userid;
        this.postingid = postingid;
    }
}
