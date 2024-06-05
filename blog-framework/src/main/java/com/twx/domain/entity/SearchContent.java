package com.twx.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户搜索内容表(SearchContent)表实体类
 *
 * @author makejava
 * @since 2024-06-05 10:56:26
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_search_content")
public class SearchContent {
//    @TableId
    private Integer id;

    //搜索内容
    private String content;
    
    private Integer count;

    public SearchContent(String content, Integer count) {
        this.content = content;
        this.count = count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
