package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.user.UserForumModule;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-22
 */
public interface UserForumModuleService extends IService<UserForumModule> {

    //查询指定用户关注了哪一些人
    PageInfo<UserForumModule> getJoinedListByUserId(String userId, int page, int row);
    //查询指定板块下都有谁入住了
    PageInfo<UserForumModule> getJoinedFanUserListByJoinedForumId(String forumId, int page,int row);
    UserForumModule getByJoinedForumIdAndUserId(String forumId, String userId);

    void transJoinedFromRedis2DB();
    void transJoinedCountFromRedis2DB();
    boolean hasUserJoin(int forumId, Integer userId);

}
