package mai.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Apply {
    private Integer id;
    private Integer userId;
    private Integer forumId;
    private String userName;
    private String ForumName;
    private Date time;
    private String reason;
}
