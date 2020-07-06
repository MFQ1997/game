package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.user.UserFollow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-22
 */
public interface UserFollowService extends IService<UserFollow> {
    PageInfo<UserFollow> getFollowedListByUserId(String likedTopicId, int page, int row);
    PageInfo<UserFollow> getFollowedFanUserListByFollwedUserId(String followedUserId, int page,int row);

    UserFollow getByFollowedUserIdAndFollowUserId(String followedUserId, String followUserId);

    void transFollowedFromRedis2DB();

    void transFollowedCountFromRedis2DB();
    //获取点赞信息
    boolean hasUserFollow(Integer followedUserId, Integer userId);
}
