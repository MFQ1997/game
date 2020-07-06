package mai.game.dto;


/*
* @Description:这个是热度为前五名的发帖量、评论量、回复量
* */
public class FiveHotForumDTO {

    private String name;
    private Integer topicCount;
    private Integer commentCount;
    private Integer replyCount;

    public FiveHotForumDTO(String name, Integer topicCount, Integer commentCount, Integer replyCount) {
        this.name = name;
        this.topicCount = topicCount;
        this.commentCount = commentCount;
        this.replyCount = replyCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(Integer topicCount) {
        this.topicCount = topicCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public String toString() {
        return "FiveHotForumDTO{" +
                "name='" + name + '\'' +
                ", topicCount='" + topicCount + '\'' +
                ", commentCount=" + commentCount +
                ", replyCount=" + replyCount +
                '}';
    }
}
