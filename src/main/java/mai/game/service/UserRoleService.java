package mai.game.service;

import mai.game.entity.po.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-16
 */
public interface UserRoleService extends IService<UserRole> {

    void insert(UserRole userRole);

    List<Integer> getRoleListByUserId(int userId);
    //修改用户的角色id为新传入的ids
    boolean changeUserRoleByUserId(Integer userId, List<Long> ids);
}
