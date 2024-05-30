package com.twx.domain.vo;

public class AddPraiseVo {
    private boolean isPraise;
    private Long articleId;

    public AddPraiseVo(boolean isPraise, Long articleId) {
        this.isPraise = isPraise;
        this.articleId = articleId;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}
