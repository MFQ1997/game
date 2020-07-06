package mai.game.service;

import mai.game.entity.po.TopicTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-25
 */
public interface TopicTagService extends IService<TopicTag> {

    List<TopicTag> selectByTopicId(Integer topicId);
}
