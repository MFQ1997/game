package mai.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReplyDTO {

    private Integer id;
    private String topicName;
    private Integer commentId;
    private String commentContent;
    private Integer replyId;
    private Integer replyType;
    private String content;
    private Integer userId;
    private String userName;
    private Integer toUserId;
    private String toUserName;
    private Date time;


}
