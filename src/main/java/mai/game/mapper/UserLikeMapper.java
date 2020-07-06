package mai.game.mapper;

import mai.game.entity.user.UserLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-28
 */
@Mapper
public interface UserLikeMapper extends BaseMapper<UserLike> {

    int hasUserZan(Integer topicId, Integer userId);
}
