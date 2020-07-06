package mai.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private Integer topicId;
    private String topicName;
    private String content;
    private Integer userId;
    private String userName;
    private Date time;

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", topicId=" + topicId +
                ", topicName='" + topicName + '\'' +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", time=" + time +
                '}';
    }
}
