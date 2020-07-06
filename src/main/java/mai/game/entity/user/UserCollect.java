package mai.game.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_collect")
@ApiModel(value="UserCollect对象", description="")
public class UserCollect extends Model<UserCollect> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer tUser;

    private Integer tTopic;

    private Integer status;

    private Date time;


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

    public Integer gettUser() {
        return tUser;
    }

    public void settUser(Integer tUser) {
        this.tUser = tUser;
    }

    public Integer gettTopic() {
        return tTopic;
    }

    public void settTopic(Integer tTopic) {
        this.tTopic = tTopic;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserCollect{" +
                "id=" + id +
                "status=" + status +
                ", tUser=" + tUser +
                ", tTopic=" + tTopic +
                ", time=" + time +
                '}';
    }
}
