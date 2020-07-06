package mai.game.mapper;

import mai.game.entity.po.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mai.game.entity.po.PermissionTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    List<PermissionTree> permissions();
    List<PermissionTree> permissionForTree(int userId);
    List<PermissionTree> permissionTree();
}
