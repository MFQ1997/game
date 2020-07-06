package mai.game.mapper;

import mai.game.entity.po.FriendUrl;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-30
 */
@Mapper
public interface FriendUrlMapper extends BaseMapper<FriendUrl> {

    List<FriendUrl> findAllFriendUrl();
}
