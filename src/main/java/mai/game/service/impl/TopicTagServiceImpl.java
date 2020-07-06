package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.entity.po.TopicTag;
import mai.game.mapper.TopicTagMapper;
import mai.game.service.TopicTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-25
 */
@Service
public class TopicTagServiceImpl extends ServiceImpl<TopicTagMapper, TopicTag> implements TopicTagService {

    @Autowired
    private TopicTagMapper topicTagMapper;

    @Override
    public List<TopicTag> selectByTopicId(Integer topicId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, topicId);
        return topicTagMapper.selectList(wrapper);
    }
}
