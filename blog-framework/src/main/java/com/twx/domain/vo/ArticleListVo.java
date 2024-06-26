package com.twx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListVo {
    private Long id;
    //标题
    private String title;
    //文章内容
    private String content;
    //文章摘要
    private String summary;
    //所属分类id
    private String categoryName;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;
    //收藏数量
    private Long stars;
    //点赞数量
    private Long praises;

    private Date createTime;

    private Long createBy;
    //文章标签
    private String tags;


}
