package mai.game.entity.vo;

import java.util.Date;

public class CommentVO {
    private Integer topicId;
    private Integer userId;
    private String userName;
    private String photo;
    private Date time;
    private String content;
    private Integer id;

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "topicId=" + topicId +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", photo='" + photo + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                ", id=" + id +
                '}';
    }
}
