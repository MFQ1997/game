package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import mai.game.entity.po.Tag;
import mai.game.entity.po.Topic;
import mai.game.entity.po.TopicTag;
import mai.game.mapper.TagMapper;
import mai.game.mapper.TopicMapper;
import mai.game.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mai.game.service.TopicTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-25
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TopicTagService topicTagService;


    @Override
    public void selectTagsByTopicId(int page, int row, int topicId) {

    }

    @Override
    public Tag selectById(Integer id) {
        return null;
    }

    @Override
    public Tag selectByName(String name) {
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Tag::getName, name);
        return tagMapper.selectOne(wrapper);
    }

    @Override
    public List<Tag> selectByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public List<Tag> selectByTopicId(Integer topicId) {
        List<TopicTag> topicTags = topicTagService.selectByTopicId(topicId);
        if (!topicTags.isEmpty()) {
            List<Integer> tagIds = topicTags.stream().map(TopicTag::getTagId).collect(Collectors.toList());
            QueryWrapper<Tag> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(Tag::getId, tagIds);
            return tagMapper.selectList(wrapper);
        }
        return Lists.newArrayList();
    }

    @Override
    public List<Tag> insertTag(List<String> tagList) {
        //存储新增成功后返回的标签对象（主要是ID）
        List<Tag> tags = new ArrayList<>();
        Iterator<String> iter = tagList.iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            Tag tag = this.selectByName(name);
            //判断数据库中是否已经存有该标签名
            if(tag==null){
                //数据库中不存在该数据，新建
                tag = new Tag();
                tag.setName(name);
                tag.setTopicCount(1);
                tag.setInTime(new Date());
                int insert = tagMapper.insert(tag);
            } else{
                //数据库中存在，则增加
                tag.setTopicCount(tag.getTopicCount() + 1);
                tagMapper.updateById(tag);
            }
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public void reduceTopicCount(Integer id) {

    }

    @Override
    public PageInfo<Topic> selectTopicByTagId(Integer tagId, int page, int row) {
        return null;
    }

    @Override
    public PageInfo<Tag> selectAll(Integer pageNo, Integer pageSize, String name) {
        return null;
    }

    @Override
    public void update(Tag tag) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void async() {

    }

    @Override
    public int countToday() {
        return 0;
    }
}
