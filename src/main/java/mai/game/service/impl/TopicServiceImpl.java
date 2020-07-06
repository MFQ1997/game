package mai.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.core.util.RecommendUtil;
import mai.game.dto.TopicInWeekDTO;
import mai.game.dto.TopicWithUserName;
import mai.game.entity.po.*;
import mai.game.entity.vo.TopicVO;
import mai.game.mapper.TagMapper;
import mai.game.mapper.TopicMapper;
import mai.game.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bytebuddy.asm.Advice;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-07
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private TopicTagService topicTagService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TopicHotWeightService topicHotWeightService;
    @Autowired
    private RedisTemplate redisTemplate;

    /*private static Date now;*/

    @Override
    public PageInfo<Topic> topicPage(int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().gt(Topic::getId,0);
        List<Topic> topics = topicMapper.selectList(queryWrapper);
        String topicJson = JSON.toJSONString(topics);
        System.out.println("将信息转为json格式"+topicJson);
        //用PageInfo对结果进行包装
        PageInfo<Topic> pageData = new PageInfo<Topic>(topics);
        return pageData;
    }

    @Override
    @Async
    @Transactional
    public Integer postTopic(TopicVO topicVO,User user) {
        //添加帖子
        Date currentTime = new Date();
        Topic topic = new Topic();
        topic.setCreateTime(currentTime);
        topic.setModifyTime(currentTime);
        topic.setForumId(topicVO.getForumId());
        topic.setClassifyId(topicVO.getClassifyId());
        topic.setTitle(topicVO.getTitle());
        topic.setContent(topicVO.getContent());
        topic.setUserId(topicVO.getUserId());
        topic.setIsDiscuss(topic.getIsDiscuss());
        topic.setIsGood(0);
        topic.setIsTop(0);
        topic.setView(0);
        topic.setCollect(0);
        topicMapper.insert(topic);
        //获取新插入的帖子所返回的id
        int topicId = topic.getId();
        //获取发布一篇帖子所获得的积分
        int create_topic_score = Integer.parseInt(systemConfigService.selectAllConfig().get("create_topic_score").toString());
        //更新用户积分
        User updateUser = new User();
        updateUser.setId(user.getId());
        //在用户已经获得的积分的基础上加上这次发布帖子的所得积分
        int original = user.getScore();
        updateUser.setScore(original+create_topic_score);
        userService.update(updateUser);

        //添加标签
        List<Tag> tags = tagService.insertTag(topicVO.getTagList());
        //绑定标签和帖子
        if (!tags.isEmpty()) {
            Iterator<Tag> iter = tags.iterator();
            while (iter.hasNext()) {
                Integer tagId = (Integer) iter.next().getId();
                TopicTag topicTag = new TopicTag();
                topicTag.setTopicId(topicId);
                topicTag.setTagId(tagId);
                topicTagService.save(topicTag);
            }
        }
        return 1;
    }

    @Override
    public PageInfo<Topic> findTopicByLikeName(String name, int page, int row) {
        PageHelper.startPage(page,row);
        List<Topic> topics = topicMapper.findTopicByLikeName(name);
        //用PageInfo对结果进行包装
        PageInfo<Topic> pageData = new PageInfo<Topic>(topics);
        return pageData;
    }

    @Override
    public List<Topic> findTopicListByTitle(String title) {
        return topicMapper.findTopicByLikeName(title);
    }

    @Override
    public int vote(Topic topic, User apiUser, HttpSession session) {
        return 0;
    }

    @Override
    public PageInfo<Topic> listProjectByPageAndLimit(int id,int page, int limit) {
        int start=(page-1)*limit;
        PageHelper.startPage(page,limit);
        List<Topic> articles = topicMapper.findAllTopic(id);
        //用PageInfo对结果进行包装
        PageInfo<Topic> pageData = new PageInfo<Topic>(articles);
        return pageData;
    }

    @Override
    public PageInfo<Topic> listProjectByPageAndLimit(int forumId, int classifyId, int page, int limit) {
        int start=(page-1)*limit;
        PageHelper.startPage(page,limit);
        List<Topic> articles = topicMapper.findAllTopicByForumIdAndClassify(forumId,classifyId);
        //用PageInfo对结果进行包装
        PageInfo<Topic> pageData = new PageInfo<Topic>(articles);
        return pageData;
    }
    @Override
    public int getCountByForumIdAndClassifyId(int forumId,int classifyId) {
        return topicMapper.findAllTopicByForumIdAndClassify(forumId,classifyId).size();
    }

    @Override
    public Integer getHotTopic() {
        //(总赞数*0.7+总评论数*0.3)*1000/(发布时间距离当前时间的小时差+2)^1.2
        return null;
    }

    @Override
    public List<Topic> getTopicListByUserId(int userId) {
        return topicMapper.getTopicByUserId(userId);
    }

    /*
    * @Description:根据用户id来分页获取用户的帖子
    * */
    @Override
    public PageInfo<Topic> getTopicByUserId(int userId,int page,int row) {
        PageHelper.startPage(page,row);
        List<Topic> topics = topicMapper.getTopicByUserId(userId);
        //用PageInfo对结果进行包装
        PageInfo<Topic> pageData = new PageInfo<Topic>(topics);
        return pageData;
    }

    @Override
    public int userTotalTopicCountNumber(int userId) {
        return topicMapper.getUserTotalTopicCountNumber(userId);
    }

    @Override
    public PageInfo<Topic> getTopicByClassifyByPageAndLimit(int classifyId, int page, int limit) {
        //构建查询条件
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Topic::getClassifyId,classifyId);

        int start=(page-1)*limit;
        PageHelper.startPage(page,limit);
        List<Topic> topicList = topicMapper.selectList(queryWrapper);
        //用PageInfo对结果进行包装
        PageInfo<Topic> pageData = new PageInfo<Topic>(topicList);
        return pageData;
    }

    /*
    * @Description:根据主键id
    * */
    @Override
    public Topic selectByTopicId(Integer topicId) {
        return topicMapper.selectById(topicId);
    }

    @Override
    public Topic getTopicById(int id) {
        Topic topicBefore = topicMapper.selectById(id);
        Topic topicAfter = new Topic();
        topicAfter.setId(id);
        topicAfter.setView(topicBefore.getView()+1);
        topicMapper.updateById(topicAfter);
        Topic topic = topicMapper.selectById(id);
        return topic;
    }


    //热度=（浏览量-1）/（发布到现在的时间间隔+2）乖以G的平方
    @Override
    public List<Topic> hotTopicRanking() {
        //将原来的清空掉
        topicHotWeightService.clearTable();
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        List<Topic> topicList = topicMapper.selectList(null);
        Iterator<Topic> iterator = topicList.iterator();
        double G = 9.8;
        while (iterator.hasNext()){
            Topic topic = iterator.next();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            try {
                //获得当前的时间
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String nowdayTime = dateFormat.format(new Date());
                Date nowDate = dateFormat.parse(nowdayTime);
                //计算热度
                long interval = RecommendUtil.getDatePoor(nowDate,topic.getCreateTime());
                int denominator = (int)Math.pow((interval+2),2);
                int molecule = (topic.getView()-1)*1000;
                int hot = molecule/denominator;
                System.out.println("帖子："+topic.getTitle()+"的热度是："+hot);
                //插入到数据库中
                TopicHotWeight topicHotWeight = new TopicHotWeight();
                topicHotWeight.setTopicId(topic.getId());
                topicHotWeight.setWeight(hot);
                topicHotWeightService.save(topicHotWeight);
                //System.out.println("帖子"+topic.getTitle()+"的时间间隔是: "+interval);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public List<Topic> getHotTopicRanking(int limit) {
        return topicMapper.getHotTopicRanking(limit);
    }

    @Override
    public List<TopicWithUserName> getHotTopicRankingWithUserImg(int limit) {
        List<TopicWithUserName> hotTopicRankingWithUserImg = topicMapper.getHotTopicRankingWithUserImg(limit);
        //将热门数据存储到Redis中
        redisTemplate.opsForList().leftPushAll(hotTopicRankingWithUserImg);
        return hotTopicRankingWithUserImg;
    }

    @Override
    public Integer getOneDay(Integer interval) {
        return topicMapper.getOneDay(interval);
    }

    @Override
    public PageInfo<Topic> getCollectedOfUserByUserId(Integer userId, int page, int limit) {
        PageHelper.startPage(page,limit);
        List<Topic> topics = topicMapper.getCollectedOfUserByUserId(userId);
        //用PageInfo对结果进行包装
        PageInfo<Topic> pageData = new PageInfo<Topic>(topics);
        return pageData;
    }

    @Override
    public List<TopicInWeekDTO> theTopicNumInThisMonth() {
        List<TopicInWeekDTO> resultList = new ArrayList<>();
        resultList.add(new TopicInWeekDTO("7天内",getOneDay(7)));
        resultList.add(new TopicInWeekDTO("15天内",getOneDay(15)));
        resultList.add(new TopicInWeekDTO("21天内",getOneDay(21)));
        resultList.add(new TopicInWeekDTO("30天内",getOneDay(30)));
        return resultList;
    }

    @Override
    public List<TopicInWeekDTO> theTopicNumInThisWeek() {
        List<TopicInWeekDTO> list = new ArrayList<>();
        list.add(new TopicInWeekDTO("1天内",getOneDay(1)));
        list.add(new TopicInWeekDTO("2天内",getOneDay(2)));
        list.add(new TopicInWeekDTO("3天内",getOneDay(3)));
        list.add(new TopicInWeekDTO("4天内",getOneDay(4)));
        list.add(new TopicInWeekDTO("5天内",getOneDay(5)));
        list.add(new TopicInWeekDTO("6天内",getOneDay(6)));
        list.add(new TopicInWeekDTO("7天内",getOneDay(7)));
        return list;
    }

    @Override
    public List<TopicInWeekDTO> theTopicNumInThisYear() {
        List<TopicInWeekDTO> list = new ArrayList<>();
        List<Map> maps = topicMapper.getOneYear();
        for (Map map : maps) {
            list.add(new TopicInWeekDTO(map.get("month").toString(),Integer.parseInt(map.get("number").toString())));
        }
        /*list.add(new TopicInWeekDTO("3月内",getOneDay(1)));
        list.add(new TopicInWeekDTO("6月内",getOneDay(2)));
        list.add(new TopicInWeekDTO("9月内",getOneDay(3)));
        list.add(new TopicInWeekDTO("12月内",getOneDay(4)));*/
        return list;
    }


}
