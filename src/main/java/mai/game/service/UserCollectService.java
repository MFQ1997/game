package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.user.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-22
 */
public interface UserCollectService extends IService<UserCollect> {

    //根据用户编号来获取他所有收藏的帖子
    PageInfo<UserCollect> getCollectedTopicListByCollectedUserId(String collectedUserId, int page, int row);
    //根据帖子的编号来获取收藏帖子的用户列表
    PageInfo<UserCollect> getCollectedUserListByCollectedTopicId(String collectedTopicId, int page,int row);

    UserCollect getByCollectedTopicIdAndCollectedUserId(String collectTopicId, String collectedUserId);

    /**
     * 将Redis里的点赞数据存入数据库中
     */
    void transCollectedFromRedis2DB();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transCollectedCountFromRedis2DB();
    //获取点赞信息
    boolean hasUserCollect(Integer topicId, Integer userId);
}
