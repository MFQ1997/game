package mai.game.core.util;

public class RedisKeyUtil {

    //保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    //保存用户被点赞数量的key
    public static final String MAP_KEY_USER_LIKED_COUNT = "MAP_USER_LIKED_COUNT";

    //保存用户的入驻板块数据的key
    public static final String MAP_KEY_USER_JOINED = "MAP_USER_JOINED";
    public static final String MAP_KEY_USER_JOINED_COUNT = "MAP_USER_JIONED_COUNT";

    //保存用户收藏贴子数据的key
    public static final String MAP_KEY_USER_COLLECTED = "MAP_USER_COLLECTED";
    public static final String MAP_KEY_USER_COLLECTED_COUNT = "MAP_USER_COLLECTED_COUNT";

    public static final String MAP_KEY_USER_FOLLOWED = "MAP_USER_FOLLOWED";
    public static final String MAP_KEY_USER_FOLLOWED_COUNT = "MAP_USER_FOLLOWED_COUNT";

    /**
     * 拼接被点赞的帖子id和点赞的用户的id作为key。格式 10::1（代表着id为1的用户点赞了id为10的帖子）
     * @param likedTopicId 被点赞的人id
     * @param likedUserId 点赞的人的id
     * @return
     */
    public static String getLikedKey(String likedTopicId, String likedUserId){
        StringBuilder builder = new StringBuilder();
        builder.append(likedTopicId);
        builder.append("::");
        builder.append(likedUserId);
        return builder.toString();
    }

    public static String getJoinedKey(String joinedForumModuleId,String joinedUserId){
        StringBuilder builder = new StringBuilder();
        builder.append(joinedForumModuleId);
        builder.append("::");
        builder.append(joinedUserId);
        return builder.toString();
    }

    public static String getCollectedKey(String CollectedTopicId,String CollectedUserId){
        StringBuilder builder = new StringBuilder();
        builder.append(CollectedTopicId);
        builder.append("::");
        builder.append(CollectedUserId);
        return builder.toString();
    }

    public static String getFollowedKey(String followedUserId,String followUserId){
        StringBuilder builder = new StringBuilder();
        builder.append(followedUserId);
        builder.append("::");
        builder.append(followUserId);
        return builder.toString();
    }
}
