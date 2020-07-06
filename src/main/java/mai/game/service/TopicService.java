package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.dto.ThreePartInWeekDTO;
import mai.game.dto.TopicInWeekDTO;
import mai.game.dto.TopicWithUserName;
import mai.game.entity.po.Topic;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.po.User;
import mai.game.entity.vo.TopicVO;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-07
 */
public interface TopicService extends IService<Topic> {

    PageInfo<Topic> topicPage(int page, int row);
    Integer postTopic(TopicVO topicVO,User user);

    PageInfo<Topic> findTopicByLikeName(String name, int page, int row);

    List<Topic> findTopicListByTitle(String title);

    int vote(Topic topic, User apiUser, HttpSession session);

    PageInfo<Topic> listProjectByPageAndLimit(int forumId,int page, int limit);
    PageInfo<Topic> listProjectByPageAndLimit(int forumId,int classifyId,int page, int limit);
    int getCountByForumIdAndClassifyId(int forumId,int classifyId);

    Integer getHotTopic();

    List<Topic> getTopicListByUserId(int userId);

    //根据用户id获取其所有的帖子
    PageInfo<Topic> getTopicByUserId(int userId,int page,int row);
    //根据用户的id获取其的总帖子数
    int userTotalTopicCountNumber(int userId);

    PageInfo<Topic> getTopicByClassifyByPageAndLimit(int classifyId, int page, int limit);

    Topic selectByTopicId(Integer topicId);

    Topic getTopicById(int id);

    //计算热度值
    List<Topic> hotTopicRanking();
    //根据热度值排序
    List<Topic> getHotTopicRanking(int limit);
    List<TopicWithUserName> getHotTopicRankingWithUserImg(int limit);

    Integer getOneDay(Integer interval);

    PageInfo<Topic> getCollectedOfUserByUserId(Integer userId, int page, int limit);

    //统计数据：月报
    List<TopicInWeekDTO> theTopicNumInThisMonth();
    //统计数据：周报
    List<TopicInWeekDTO> theTopicNumInThisWeek();
    //统计数据：年报
    List<TopicInWeekDTO> theTopicNumInThisYear();


    //根据id获取帖子
}
