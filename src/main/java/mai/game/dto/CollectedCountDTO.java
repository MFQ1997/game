package mai.game.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 入驻数量 DTO。用于存储从 Redis 取出来的被入驻数量
 */
@Data
public class CollectedCountDTO implements Serializable {

    private static final long serialVersionUID = -2856160546081194945L;


    private String id;

    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public CollectedCountDTO() {
    }

    public CollectedCountDTO(String id, Integer count) {
        this.id = id;
        this.count = count;
    }
}
