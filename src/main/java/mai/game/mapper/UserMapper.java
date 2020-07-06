package mai.game.mapper;

import mai.game.dto.MasterDTO;
import mai.game.entity.po.Role;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.Topic;
import mai.game.entity.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

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
public interface UserMapper extends BaseMapper<User> {
    //获取所有的角色
    List<User> findAllRole();

    List<User> findUserByLikeName(String name);

    int userCount();

    User findUserToLogin(String phone);

    //只是获取用户的个人信息，不查询角色以及权限信息
    SimpleUser selectBySimpleUserId(Integer id);

    SimpleUser isNullByEmail(String email);

    SimpleUser findSimpleUserByEmail(String username);

    boolean updatePassword(@RequestBody SimpleUser user);

    MasterDTO selectMasterByUserId(Integer userId);

    SimpleUser selectSimpleUserByEmail(String email);

    boolean updateSimpleById(SimpleUser user);
}
