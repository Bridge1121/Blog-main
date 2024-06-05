package entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 用户历史浏览记录表(UserHistoryArticle)表实体类
 *
 * @author makejava
 * @since 2024-06-05 14:57:33
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_user_history_article")
public class UserHistoryArticle  {
    @TableId
    private Long articleid;

    
    private Long userid;
    
    private String time;



}
