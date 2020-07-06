package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.core.util.TreeUtil;
import mai.game.entity.po.Permission;
import mai.game.entity.po.PermissionTree;
import mai.game.entity.po.RolePermission;
import mai.game.mapper.PermissionMapper;
import mai.game.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionServiceImpl rolePermissionService;


    // 根据角色id查询所有的权限
    @Override
    public List<Permission> selectByRoleId(Integer roleId) {
        List<RolePermission> rolePermissions = rolePermissionService.selectByRoleId(roleId);
        List<Integer> permissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors
                .toList());
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(Permission::getId, permissionIds);
        return permissionMapper.selectList(wrapper);
    }

    /*
     * @Name:Permissions
     * @Description:获取树形的权限集合
     * @Date:2019
     * */
    @Override
    public List<PermissionTree> Permissions(int userId) {
        //从数据库中查询出所有的权限
        List<PermissionTree> permissionList = permissionMapper.permissionForTree(userId);
        System.out.println("从数据库中查询出来的所有权限"+permissionList);
        //将上一步找出的所有权限做递归排序父子节点，并且返回树的形式的结果集合
        Collection<PermissionTree> permissions = TreeUtil.toTree(permissionList, "id", "pid", "children", PermissionTree.class);
        List resultToTreelist;
        if (permissions instanceof List)
            resultToTreelist = (List)permissions;
        else
            resultToTreelist = null;
            //resultToTreelist = new ArrayList(permissions);

        //返回树形权权限集合
        return resultToTreelist;
    }
    /*
     * @Name:Permissions
     * @Description:获取树形的权限集合
     * @Date:2019
     * */
    @Override
    public List<PermissionTree> Permissions() {
        //从数据库中查询出所有的权限
        List<PermissionTree> permissionList = permissionMapper.permissionTree();
        //将上一步找出的所有权限做递归排序父子节点，并且返回树的形式的结果集合
        Collection<PermissionTree> permissions = TreeUtil.toTree(permissionList, "id", "pid", "children", PermissionTree.class);
        //将collection转化为List格式
        System.out.println("在这里看看有没有执行到");
        List resultToTreelist;
        if (permissions instanceof List)
            resultToTreelist = (List)permissions;
        else
            resultToTreelist = new ArrayList(permissions);
        //返回树形权权限集合
        return resultToTreelist;
    }

    // 根据父节点查询子节点
    @Override
    public List<Permission> selectByPid(Integer pid) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Permission::getPid, pid);
        return permissionMapper.selectList(wrapper);
    }

    @Override
    public Map<String, List<Permission>> selectAll() {
        Map<String, List<Permission>> map = new LinkedHashMap<>();
        // 先查父节点
        List<Permission> permissions = this.selectByPid(0);
        // 再查子节点
        permissions.forEach(permission -> map.put(permission.getPermissionName(), this.selectByPid(permission.getId())));
        return map;
    }

    @Override
    public Permission insert(Permission permission) {
        permissionMapper.insert(permission);
        return permission;
    }

    @Override
    public Permission update(Permission permission) {
        permissionMapper.updateById(permission);
        return permission;
    }

    @Override
    public Boolean delete(Integer id) {
        Permission permission = permissionMapper.selectById(id);
        // 如果是父节点的话，要把所有子节点下的所有关联角色的记录都删了，否则会报错
        if (permission.getPid() == 0) {
            List<Permission> permissions = this.selectByPid(permission.getId());
            permissions.forEach(permission1 -> {
                // 先删除role_permission里的关联数据
                rolePermissionService.deleteByPermissionId(permission1.getId());
                // 删除子节点
                permissionMapper.deleteById(permission1.getId());
            });
        } else {
            // 先删除role_permission里的关联数据
            rolePermissionService.deleteByPermissionId(id);
        }
        // 删除自己
        permissionMapper.deleteById(id);
        return true;
    }

    @Override
    public List<Permission> getList() {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(Permission::getSort);
        List<Permission> list = permissionMapper.selectList(queryWrapper);
        return list;
    }
}
