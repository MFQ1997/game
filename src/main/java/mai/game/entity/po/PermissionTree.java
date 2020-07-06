package mai.game.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
public class PermissionTree extends Model<PermissionTree> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表的主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "权限的名称")
    private String name;

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

    /*
     * @Description:这个是存储字菜单的
     * */
    private List<PermissionTree> children;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<PermissionTree> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionTree> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "PermissionTree{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                ", url='" + url + '\'' +
                ", perms='" + perms + '\'' +
                ", type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                ", sort=" + sort +
                ", status=" + status +
                ", kind=" + kind +
                ", remark='" + remark + '\'' +
                ", children=" + children +
                '}';
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
