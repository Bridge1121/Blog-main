package com.twx.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {
    private Long id;

    //访问量
    private Long viewCount;
    //收藏数量
    private Long stars;
    //点赞数量
    private Long praises;
    //标题
    private String title;
    //文章标签
    private String tags;
}
