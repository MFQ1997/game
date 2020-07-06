package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.user.UserLike;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-28
 */
public interface UserLikeService extends IService<UserLike> {

    /**
     * 保存点赞记录
     *
     * @param userLike
     * @return
     */
    UserLike saveOne(UserLike userLike);

    /**
     * 批量保存或修改
     *
     * @param list
     */
    List<UserLike> saveAll(List<UserLike> list);


    /**
     * 根据被点赞帖子的id查询点赞列表（即查询都谁给这篇帖子点赞过）
     *
     * @param likedTopicId 被点赞帖子的id
     * @param PageInfo
     * @return
     */
    PageInfo<UserLike> getLikedUserListByLikedTopicId(String likedTopicId, int page,int row);

    /**
     * 根据点赞人的id查询点赞列表（即查询这个人都哪一些帖子点赞过）
     *
     * @param likedUserId
     * @param pageable
     * @return
     */
    PageInfo<UserLike> getLikedTopicListByLikedUserId(String likedUserId, int page,int row);

    /**
     * 通过被点赞的帖子和点赞人id查询是否存在点赞记录
     *
     * @param likedTopicId
     * @param likedUserId
     * @return
     */
    UserLike getByLikedTopicIdAndLikedUserId(String likedTopicId, String likedUserId);

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void transLikedFromRedis2DB();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transLikedCountFromRedis2DB();
    //获取点赞信息
    boolean hasUserZan(Integer topicId, Integer userId);
}
