package mai.game.service;

import mai.game.entity.po.Collect;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.po.User;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-27
 */
public interface CollectService extends IService<Collect> {

    // 查询话题被多少人收藏过
    List<Collect> selectByTopicId(Integer topicId);

    // 查询用户是否收藏过某个话题
    Collect selectByTopicIdAndUserId(Integer topicId, Integer userId);

    // 收藏话题
    Collect insert(Integer topicId, User user);

    // 删除（取消）收藏
    void delete(Integer topicId, Integer userId);

    // 根据话题id删除收藏记录
    void deleteByTopicId(Integer topicId);

    // 根据用户id删除收藏记录
    void deleteByUserId(Integer userId);

    // 查询用户收藏的话题数
    int countByUserId(Integer userId);

    // 查询用户收藏的话题
    //MyPage<Map<String, Object>> selectByUserId(Integer userId, Integer pageNo, Integer pageSize);

}
