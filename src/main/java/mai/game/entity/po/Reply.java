package mai.game.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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
 * @since 2020-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_reply")
@ApiModel(value="Reply对象", description="")
public class Reply extends Model<Reply> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "评论的编号")
    private Integer commentId;

    @ApiModelProperty(value = "回复目标的编号")
    private Integer replyId;

    @ApiModelProperty(value = "0表示是评论的回复，1表示是回复的回复")
    private Integer replyType;

    @ApiModelProperty(value = "回复内容")
    private String content;

    @ApiModelProperty(value = "回复者的编号")
    private Integer userId;

    @ApiModelProperty(value = "目标用户的编号")
    private Integer toUserId;

    @ApiModelProperty(value = "回复时间")
    private Date time;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
