package mai.game.service;

import mai.game.entity.po.TopicHotWeight;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-06
 */
public interface TopicHotWeightService extends IService<TopicHotWeight> {

    public boolean clearTable();

}
