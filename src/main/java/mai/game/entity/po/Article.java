package mai.game.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_article")
@ApiModel(value="Article对象", description="")
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增长的id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "分类的分类：0表示是社区的资讯分类，1表示是论坛的板块分类")
    private Integer classifyId;

    @ApiModelProperty(value = "作者")
    private Integer author;

    @ApiModelProperty(value = "时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date time;

    private String img;

    @ApiModelProperty(value = "文章状态（0表示未审核，1表示审核未通过，2表示审核通过并发布）")
    private Integer status;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "资讯内容")
    private String content;

    @ApiModelProperty(value = "阅读量")
    private Integer view;

    @ApiModelProperty(value = "是否置顶")
    private Integer isTop;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
