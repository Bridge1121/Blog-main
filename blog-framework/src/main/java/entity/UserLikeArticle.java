package entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (UserLikeArticle)表实体类
 *
 * @author makejava
 * @since 2024-05-27 14:06:38
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_user_like_article")
public class UserLikeArticle  {
    @TableId
    private Long userid;
    @TableId
    private Long articleid;




}
