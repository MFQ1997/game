package mai.game.core.enums;

import lombok.Getter;

@Getter
public enum JoinedStatusEnum {
    JOIN(1, "入驻板块"),
    UNJOIN(0, "取消入驻/未入驻"),
    ;

    private Integer code;

    private String msg;

    JoinedStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
