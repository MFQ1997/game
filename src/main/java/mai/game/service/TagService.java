package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.po.Topic;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-25
 */
public interface TagService extends IService<Tag> {
    void selectTagsByTopicId(int page,int row,int topicId);
    Tag selectById(Integer id);
    Tag selectByName(String name);
    List<Tag> selectByIds(List<Integer> ids);
    // 根据话题查询关联的所有标签
    List<Tag> selectByTopicId(Integer topicId);
    // 将创建话题时填的tag处理并保存
    List<Tag> insertTag(List<String> tagList);
    // 将标签的话题数都-1
    void reduceTopicCount(Integer id);

    // 查询标签关联的话题
    PageInfo<Topic> selectTopicByTagId(Integer tagId,int page,int row);

    // 查询标签列表
    PageInfo<Tag> selectAll(Integer pageNo, Integer pageSize, String name);

    void update(Tag tag);

    // 如果 topic_tag 表里还有关联的数据，这里删除会报错
    void delete(Integer id);

    //同步标签的话题数
    void async();

    // 查询今天新增的标签数
    int countToday();
}
