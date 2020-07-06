package mai.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Role;
import mai.game.entity.po.RolePermission;
import mai.game.entity.vo.RoleVO;
import mai.game.mapper.RoleMapper;
import mai.game.mapper.RolePermissionMapper;
import mai.game.service.RolePermissionService;
import mai.game.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RolePermissionService rolePermissionService;

    @Override
    public Role selectById(Integer roleId) {
        return roleMapper.getById(roleId);
    }

    @Override
    public List<Role> selectAll() {
        return null;
    }

    @Override
    public PageInfo<Role> rolePage(int page, int row) {
        PageHelper.startPage(page,row);
        List<Role> roles = roleMapper.findAllRole();
        String userJson = JSON.toJSONString(roles);
        System.out.println("将信息转为json格式"+userJson);
        //用PageInfo对结果进行包装
        PageInfo<Role> pageData = new PageInfo<Role>(roles);
        return pageData;
    }

    @Override
    @Transactional
    public void insert(RoleVO roleVO) {
        Role role = new Role();
        //将角色信息插入到角色表
        role.setRoleName(roleVO.getRoleName());
        role.setStatus(roleVO.getStatus());
        role.setRemark(roleVO.getRemark());
        //获取刚插入的数据的主键id
        roleMapper.insert(role);
        int roleId = role.getId();
        System.out.println("回显的的角色Id"+roleId);
        System.out.println("新插入的角色id是"+roleId);

        Iterator<Integer> iter = roleVO.getPermissions().iterator();
        while (iter.hasNext()) {
            Integer id = (Integer) iter.next();
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(id);
            rolePermissionService.insert(rolePermission);
            //返回插入成功的信息
        }
    }

    @Override
    public void update(Integer id, String name, Integer[] permissionIds) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public PageInfo<Role> findRoleByLikeName(String name, int page, int row) {
        PageHelper.startPage(page,row);
        List<Role> users = roleMapper.findUserByLikeName(name);
        //用PageInfo对结果进行包装
        PageInfo<Role> pageData = new PageInfo<Role>(users);
        return pageData;
    }

    @Override
    public boolean updateRoleById(RoleVO roleVO) {
        //设置角色的信息
        Role role = new Role();
        role.setRoleName(roleVO.getRoleName());
        role.setId(roleVO.getId());
        role.setStatus(roleVO.getStatus());
        role.setRemark(roleVO.getRemark());
        roleMapper.updateById(role);
        int roleId = role.getId();
        //开始更新角色-权限的中间表信息
        //首先根据角色id把中间表的信息进行删除
        rolePermissionService.deleteByRoleId(roleVO.getId());
        Iterator<Integer> iter = roleVO.getPermissions().iterator();
        while (iter.hasNext()) {
            Integer id = (Integer) iter.next();
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(id);
            rolePermissionService.insert(rolePermission);
            //返回插入成功的信息
        }
        return true;
    }
}
