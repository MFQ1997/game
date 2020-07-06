package mai.game.mapper;

import mai.game.entity.po.SystemLog;
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
public interface SystemLogMapper extends BaseMapper<SystemLog> {

    public List<SystemLog> findAllLog();

    List<Integer> getUserForumHostoryByEmail(String email);
}
