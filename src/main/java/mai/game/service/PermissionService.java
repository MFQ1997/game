package mai.game.service;

import mai.game.entity.po.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.po.PermissionTree;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
public interface PermissionService extends IService<Permission> {

    // 根据角色id查询所拥有的权限
    List<Permission> selectByRoleId(Integer roleId);

    List<PermissionTree> Permissions(int userId);
    List<PermissionTree> Permissions();
    // 根据父节点查询子节点
    List<Permission> selectByPid(Integer pid);

    Map<String, List<Permission>> selectAll();

    Permission insert(Permission permission);

    Permission update(Permission permission);

    Boolean delete(Integer id);

    List<Permission> getList();
}
