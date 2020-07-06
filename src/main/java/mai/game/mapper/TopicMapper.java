package mai.game.mapper;

import mai.game.dto.TopicWithUserName;
import mai.game.entity.po.Topic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-07
 */

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {

    List<Topic> findAllTopic(Integer forumId);

    List<Topic> findAllTopicByForumIdAndClassify(int forumId, int classifyId);

    List<Topic> findTopicByLikeName(String name);

    List<Topic> getTopicByUserId(int userId);

    int getUserTotalTopicCountNumber(int userId);

    List<Topic> getHotTopicRanking(int limit);

    List<TopicWithUserName> getHotTopicRankingWithUserImg(int limit);

    Integer getOneDay(Integer interval);
    List<Map> getOneYear();

    List<Topic> getCollectedOfUserByUserId(Integer userId);
}
