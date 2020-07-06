package mai.game.core.enums;

import lombok.Getter;

/**
 * 用户点赞的状态
 */
@Getter
public enum CollectedStatusEnum {
    COLLECT(1, "收藏"),
    UNCOLLECT(0, "取消收藏/未收藏"),
    ;

    private Integer code;

    private String msg;

    CollectedStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
