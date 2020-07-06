package mai.game.service;

import mai.game.dto.CollectedCountDTO;
import mai.game.dto.FollowedCountDTO;
import mai.game.dto.JoinedCountDTO;
import mai.game.dto.LikedCountDTO;
import mai.game.entity.user.UserCollect;
import mai.game.entity.user.UserFollow;
import mai.game.entity.user.UserForumModule;
import mai.game.entity.user.UserLike;

import java.util.List;

public interface RedisService {
    /**
     * 点赞。状态为1
     *
     * @param likedUserId
     * @param likedPostId
     */
    void saveLiked2Redis(String likedUserId, String likedPostId);

    /**
     * 取消点赞。将状态改变为0
     *
     * @param likedUserId
     * @param likedPostId
     */
    void unlikeFromRedis(String likedUserId, String likedPostId);

    /**
     * 从Redis中删除一条点赞数据
     *
     * @param likedUserId
     * @param likedPostId
     */
    void deleteLikedFromRedis(String likedUserId, String likedPostId);

    /**
     * 该用户的点赞数加1
     *
     * @param likedUserId
     */
    void incrementLikedCount(String likedUserId);

    /**
     * 该用户的点赞数减1
     *
     * @param likedUserId
     */
    void decrementLikedCount(String likedUserId);

    /**
     * 获取Redis中存储的所有点赞数据
     *
     * @return
     */
    List<UserLike> getLikedDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     *
     * @return
     */
    List<LikedCountDTO> getLikedCountFromRedis();




    /*
    * ———————————————————————————————————————————————————————————————— 入驻板块
    * */
    void saveJoin2Redis(String joinedForumModuleId,String joinedUserId);
    void unJoinFromRedis(String joinedForumModuleId,String joinedUserId);
    void deleteJoinedFromRedis(String joinedForumModuleId,String joinedUserId);
    void incrementJoinedCount(String joinedForumModuleId);
    void decrementJoinedCount(String joinedForumModuleId);
    List<UserForumModule> getJoinedDataFromRedis();
    List<JoinedCountDTO> getJoinedCountFromRedis();

    /*
    * _________________________________________________________________关注
    * */
    void saveFollow2Redis(String followedUserId,String followUserId);
    void unFollowFromRedis(String followedUserId,String followUserId);
    void deleteFollowedFromRedis(String followedUserId,String followUserId);
    void incrementFollowedCount(String followedUserId);
    void decrementFollowedCount(String followedUserId);
    List<UserFollow> getFollowedDataFromRedis();
    List<FollowedCountDTO> getFollowedCountFromRedis();

    /*
    * —————————————————————————————————————————————————————————————————收藏
    * */
    void saveCollect2Redis(String CollectedTopicId,String CollectedUserId);
    void unCollectFromRedis(String CollectedTopicId,String CollectedUserId);
    void deleteCollectedFromRedis(String CollectedTopicId,String CollectedUserId);
    void incrementCollectedCount(String CollectedTopicId);
    void decrementCollectedCount(String CollectedTopicId);
    List<UserCollect> getCollectedDataFromRedis();
    List<CollectedCountDTO> getCollectedCountFromRedis();

    /*
     *  判断redis中是否存在用户的数据
     * */
    Integer hasUserZan(Integer topicId, Integer userId);
    Integer hasUserCollect(Integer topicId, Integer userId);
    Integer hasUserFollow(Integer masterId, Integer userId);
    Integer hasUserJoin(int forumId, Integer userId);

    //查询Redis存储指定用户的板块信息
    List<UserForumModule> getUserJoinedDataFromRedis(Integer userId);

    String getLikedCountFromRedisByTopicId(Integer id);
}
