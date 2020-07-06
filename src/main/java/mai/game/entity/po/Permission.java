package mai.game.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

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
 * @since 2020-02-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_permission")
@ApiModel(value="Permission对象", description="")
public class Permission extends Model<Permission> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表的主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限的名称")
    private String permissionName;

    @ApiModelProperty(value = "父类的id")
    private Integer pid;

    @ApiModelProperty(value = "挑战的url")
    private String url;

    @ApiModelProperty(value = "标记权限")
    private String perms;

    private String type;

    @ApiModelProperty(value = "icon图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "权限的状态")
    private Integer status;

    @ApiModelProperty(value = "0表示是公有的模块，1表示是平台的模块，2表示是公司的模块")
    private Integer kind;

    @ApiModelProperty(value = "注释")
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
