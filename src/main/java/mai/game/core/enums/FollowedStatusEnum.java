package mai.game.core.enums;

import lombok.Getter;

/**
 * 用户点赞的状态
 */
@Getter
public enum FollowedStatusEnum {
    FOLLOW(1, "关注"),
    UNFOLLOW(0, "取消关注/未关注"),
    ;

    private Integer code;

    private String msg;

    FollowedStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
