package mai.game.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2020-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_friend_url")
@ApiModel(value="FriendUrl对象", description="")
public class FriendUrl extends Model<FriendUrl> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增长的Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "友情链接的url")
    private String url;

    @ApiModelProperty(value = "友情链接的名称")
    private String name;

    private String remark;

    @ApiModelProperty(value = "1表示启用，0表示禁用")
    private Integer status;

    @ApiModelProperty(value = "0表示是社区的友情链接，1表示是论坛的友情链接")
    private Integer classify;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
