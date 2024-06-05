package entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class SearchContent  {
    @TableId
    private Integer id;

    //搜索内容
    private String content;
    
    private Integer count;



}
