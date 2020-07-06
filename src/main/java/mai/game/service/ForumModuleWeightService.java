package mai.game.service;

import mai.game.entity.po.ForumModuleWeight;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-09
 */
public interface ForumModuleWeightService extends IService<ForumModuleWeight> {
    public boolean clearTable();
}
