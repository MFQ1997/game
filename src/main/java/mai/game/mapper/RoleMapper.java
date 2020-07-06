package mai.game.mapper;

import mai.game.entity.po.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface RoleMapper extends BaseMapper<Role> {
    //获取所有的角色
    public List<Role> findAllRole();

    List<Role> findUserByLikeName(String name);

    Role getById(Integer roleId);
}
