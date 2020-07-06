package mai.game.mapper;

import mai.game.entity.po.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-16
 */

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<Integer> getRoleListByUserId(int userId);

    boolean deleteAllByUserId(Integer userId);
}
