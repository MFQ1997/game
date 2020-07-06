package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.vo.RoleVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
public interface RoleService extends IService<Role> {
    Role selectById(Integer roleId);

    List<Role> selectAll();
    PageInfo<Role> rolePage(int page,int row);
    void insert(RoleVO roleVO);

    void update(Integer id, String name, Integer[] permissionIds);

    void delete(Integer id);

    PageInfo<Role> findRoleByLikeName(String name, int page, int row);

    boolean updateRoleById(RoleVO roleVO);
}
