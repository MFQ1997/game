package mai.game.dto;

public class MasterDTO {

    private String img;             //帖子主人的头像
    private Integer userId;         //用户id
    private String userName;        //名字
    private Integer topicCount;     //发帖量
    private Integer commentCount;   //评论量
    private Integer fanCount;            //粉丝量
    private Integer replyCount;          //回复量
    private Integer voteCount;      //点赞量

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    @Override
    public String toString() {
        return "MasterDTO{" +
                "img='" + img + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", topicCount=" + topicCount +
                ", commentCount=" + commentCount +
                ", fanCount=" + fanCount +
                ", replyCount=" + replyCount +
                ", voteCount=" + voteCount +
                '}';
    }
}
