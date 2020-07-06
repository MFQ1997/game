package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.entity.po.Permission;
import mai.game.entity.po.RolePermission;
import mai.game.mapper.RolePermissionMapper;
import mai.game.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-08
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    // 根据角色id查询所有的角色权限关联记录
    @Override
    public List<RolePermission> selectByRoleId(Integer roleId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        return rolePermissionMapper.selectList(wrapper);
    }

    @Override
    public List<Permission> getPermissionByRoleId(Integer roleId) {
        return null;
    }

    // 根据角色id删除关联关系
    @Override
    public void deleteByRoleId(Integer roleId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getRoleId, roleId);
        rolePermissionMapper.delete(wrapper);
    }

    // 根据权限id删除关联关系
    @Override
    public void deleteByPermissionId(Integer permissionId) {
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RolePermission::getPermissionId, permissionId);
        rolePermissionMapper.delete(wrapper);
    }

    @Override
    public void insert(RolePermission rolePermission) {
        rolePermissionMapper.insert(rolePermission);
    }

    @Override
    public int[] getPermissionArrayByRoleId(Integer roleId) {
        List<Integer> checkedList =  rolePermissionMapper.getPermissionArrayByRoleId(roleId);
        //将List格式的转为Integer
        Integer[] integers = checkedList.toArray(new Integer[checkedList.size()]);
        int[] checked = new int[integers.length];
        //将Integer数组转为int
        for(int i = 0; i < integers.length; i ++){
            checked[i] = integers[i] == null ? 0 : integers[i];
        }
        return checked;
    }

}
