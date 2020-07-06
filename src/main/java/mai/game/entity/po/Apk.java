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
 * @since 2020-05-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_apk")
@ApiModel(value="Apk对象", description="")
public class Apk extends Model<Apk> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String img;

    @ApiModelProperty(value = "安装包名称")
    private String name;

    @ApiModelProperty(value = "保存路径")
    private String url;

    private int size;

    @ApiModelProperty(value = "上传时间")
    private Date time;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "下载量")
    private Integer num;

    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
