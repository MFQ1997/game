package mai.game.mapper;

import mai.game.entity.po.TopicHotWeight;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-06
 */

@Mapper
public interface TopicHotWeightMapper extends BaseMapper<TopicHotWeight> {

    boolean clearTable();
}
