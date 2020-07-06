package mai.game.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_system_config")
@ApiModel(value="SystemConfig对象", description="")
public class SystemConfig extends Model<SystemConfig> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 配置键
    @TableField("`key`")
    private String key;

    // 配置值
    @TableField("`value`")
    private String value;


    private Integer pid;

    // 配置类型，常见的有 select, input[type=text,url,number,radio,password,email]
    @TableField("`type`")
    private String type;

    // 特殊类型里的值，比如 radio，select 的option
    @TableField("`option`")
    private String option;

    // 修改后是否需要重启
    @TableField("`reboot`")
    private Integer reboot;

    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Integer getReboot() {
        return reboot;
    }

    public void setReboot(Integer reboot) {
        this.reboot = reboot;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "SystemConfig{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", pid=" + pid +
                ", type='" + type + '\'' +
                ", option='" + option + '\'' +
                ", reboot=" + reboot +
                ", remark='" + remark + '\'' +
                '}';
    }
}
