package com.twx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentVo {
    private Long id;

    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;
    private int prizes;//当前评论点赞数

    private Long createBy;

    private Date createTime;

    private String userName;//评论人用户名
    private List<CommentReplyVo> replies;

    /**
     * 当前页码
     */
    public int currentPage;
    /**
     * 每一页数据的大小
     */
    private int pageSize;
    /**
     * 总页数
     */
    public int totalPages;
    /**
     * 数据总数
     */
    public int totalDataSize;
    /**
     * 下一个页码
     */
    public int nextPage;
    /**
     * 上一个页码
     */
    public int prefPage;

    public int getPrizes() {
        return prizes;
    }

    public void setPrizes(int prizes) {
        this.prizes = prizes;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalDataSize() {
        return totalDataSize;
    }

    public void setTotalDataSize(int totalDataSize) {
        this.totalDataSize = totalDataSize;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrefPage() {
        return prefPage;
    }

    public void setPrefPage(int prefPage) {
        this.prefPage = prefPage;
    }

    public List<CommentReplyVo> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentReplyVo> replies) {
        this.replies = replies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getToCommentUserId() {
        return toCommentUserId;
    }

    public void setToCommentUserId(Long toCommentUserId) {
        this.toCommentUserId = toCommentUserId;
    }

    public String getToCommentUserName() {
        return toCommentUserName;
    }

    public void setToCommentUserName(String toCommentUserName) {
        this.toCommentUserName = toCommentUserName;
    }

    public Long getToCommentId() {
        return toCommentId;
    }

    public void setToCommentId(Long toCommentId) {
        this.toCommentId = toCommentId;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
