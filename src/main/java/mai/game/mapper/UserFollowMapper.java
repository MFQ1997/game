package mai.game.mapper;

import mai.game.entity.user.UserFollow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-22
 */

@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {

    int hasUserFollow(Integer followedUserId, Integer userId);
}
