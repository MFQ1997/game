package mai.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ForumModuleDTO {

    private Integer id;
    private String name;
    private String photo;
    private Integer master;
    private String masterName;
    private Date createTime;
    private Integer status;
    private String remark;
    private Integer view;

    @Override
    public String toString() {
        return "ForumModuleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", photo='" + photo + '\'' +
                ", master=" + master +
                ", masterName='" + masterName + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", view=" + view +
                '}';
    }
}
