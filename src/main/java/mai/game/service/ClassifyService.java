package mai.game.service;

import mai.game.entity.po.Classify;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.po.ClassifyTree;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-07
 */
public interface ClassifyService extends IService<Classify> {

    List<Classify> getClassifyByClassifyId(Integer id);
    List<ClassifyTree> classifies();
}
