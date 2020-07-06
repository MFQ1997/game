package mai.game.mapper;

import mai.game.entity.po.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-08
 */

@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Integer> getPermissionArrayByRoleId(Integer roleId);
}
