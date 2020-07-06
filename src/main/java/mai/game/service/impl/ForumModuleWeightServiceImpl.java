package mai.game.service.impl;

import mai.game.entity.po.ForumModuleWeight;
import mai.game.mapper.ForumModuleMapper;
import mai.game.mapper.ForumModuleWeightMapper;
import mai.game.service.ForumModuleWeightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-09
 */
@Service
public class ForumModuleWeightServiceImpl extends ServiceImpl<ForumModuleWeightMapper, ForumModuleWeight> implements ForumModuleWeightService {

    @Autowired
    private ForumModuleWeightMapper forumModuleWeightMapper;

    @Override
    public boolean clearTable() {
        return forumModuleWeightMapper.clearTable();
    }
}
