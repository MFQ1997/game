package mai.game.service.impl;

import mai.game.entity.po.UserRole;
import mai.game.mapper.UserRoleMapper;
import mai.game.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-16
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void insert(UserRole userRole) {
        userRoleMapper.insert(userRole);
    }

    @Override
    public List<Integer> getRoleListByUserId(int userId) {
        return userRoleMapper.getRoleListByUserId(userId);
    }

    @Override
    public boolean changeUserRoleByUserId(Integer userId, List<Long> ids) {
        //先把数据库中存在的用户和角色信息删除
        userRoleMapper.deleteAllByUserId(userId);
        Iterator<Long> iterator = ids.iterator();
        while (iterator.hasNext()){
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(iterator.next().intValue());
            userRoleMapper.insert(userRole);
        }
        return true;
    }
}
