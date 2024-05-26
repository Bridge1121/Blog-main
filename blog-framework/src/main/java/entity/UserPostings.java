package entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (UserPostings)表实体类
 *
 * @author makejava
 * @since 2024-05-26 12:57:59
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_user_postings")
public class UserPostings  {
    @TableId
    private Long id;

    //动态包含的图片
    private String images;
    //用户动态用点赞数量
    private Integer praises;
    //动态文字
    private String content;
    
    private Date createTime;
    
    private Long createBy;
    
    private Integer delFlag;



}
