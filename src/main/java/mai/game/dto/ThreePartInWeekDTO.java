package mai.game.dto;

import javax.naming.Name;

/*
* @Description:这是一周之内的总发帖量、回复量、评论量的实体类
* */
public class ThreePartInWeekDTO {
    private String name;
    private Integer value;

    public ThreePartInWeekDTO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ThreePartInWeekDTO{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
