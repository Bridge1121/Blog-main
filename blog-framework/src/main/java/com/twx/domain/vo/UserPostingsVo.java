package com.twx.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;

public class UserPostingsVo {
    @TableId
    private Long id;

    //动态包含的图片
    private String images;
    //用户动态用点赞数量
    private Integer praises;
    //动态文字
    private String content;

    private Long createBy;
    private String createUserName;//创建用户名称
    private String avatar;//创建用户头像
    private Date createTime;

    private int comments;//当前动态评论总数
    private boolean isPraise;//判断当前用户是否对该动态点赞

    private Integer delFlag;

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getPraises() {
        return praises;
    }

    public void setPraises(Integer praises) {
        this.praises = praises;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
