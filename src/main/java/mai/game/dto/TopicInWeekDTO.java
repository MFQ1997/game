package mai.game.dto;

/*
* @Description:这是本周的帖子发布量的实体类
* */

public class TopicInWeekDTO {

    private String day;//星期几
    private Integer num;

    public TopicInWeekDTO(String day, Integer num) {
        this.day = day;
        this.num = num;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "TopicInWeekDTO{" +
                "day='" + day + '\'' +
                ", num=" + num +
                '}';
    }
}
