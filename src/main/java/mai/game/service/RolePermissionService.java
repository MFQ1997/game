package mai.game.service;

import mai.game.entity.po.Permission;
import mai.game.entity.po.RolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-08
 */
public interface RolePermissionService extends IService<RolePermission> {
    // 根据角色id查询所有的角色权限关联记录
    List<RolePermission> selectByRoleId(Integer roleId);
    List<Permission> getPermissionByRoleId(Integer roleId);

    // 根据角色id删除关联关系
    void deleteByRoleId(Integer roleId);

    // 根据权限id删除关联关系
    void deleteByPermissionId(Integer permissionId);

    void insert(RolePermission rolePermission);

    int[] getPermissionArrayByRoleId(Integer roleId);
}
