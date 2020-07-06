package mai.game.service.impl;

import mai.game.entity.po.TopicHotWeight;
import mai.game.mapper.TopicHotWeightMapper;
import mai.game.service.TopicHotWeightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-06
 */
@Service
public class TopicHotWeightServiceImpl extends ServiceImpl<TopicHotWeightMapper, TopicHotWeight> implements TopicHotWeightService {

    @Autowired
    private TopicHotWeightMapper topicHotWeightMapper;

    @Override
    public boolean clearTable() {
        return topicHotWeightMapper.clearTable();
    }
}
