package mai.game.mapper;

import mai.game.entity.po.Classify;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mai.game.entity.po.ClassifyTree;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-07
 */

@Mapper
public interface ClassifyMapper extends BaseMapper<Classify> {

    List<ClassifyTree> classifyForTree();
}
