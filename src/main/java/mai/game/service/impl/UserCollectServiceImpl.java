package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.dto.CollectedCountDTO;
import mai.game.entity.po.Topic;
import mai.game.entity.user.UserCollect;
import mai.game.mapper.UserCollectMapper;
import mai.game.service.RedisService;
import mai.game.service.TopicService;
import mai.game.service.UserCollectService;
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
 * @since 2020-05-22
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserCollectMapper userCollectMapper;
    @Autowired
    private TopicService topicService;

    @Override
    public PageInfo<UserCollect> getCollectedTopicListByCollectedUserId(String collectedUserId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCollect::gettUser,collectedUserId);
        List<UserCollect> userLikeList = userCollectMapper.selectList(queryWrapper);
        PageInfo<UserCollect> pageData = new PageInfo<UserCollect>(userLikeList);
        return pageData;
    }

    @Override
    public PageInfo<UserCollect> getCollectedUserListByCollectedTopicId(String collectedTopicId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCollect::gettTopic,collectedTopicId);
        List<UserCollect> userLikeList = userCollectMapper.selectList(queryWrapper);
        PageInfo<UserCollect> pageData = new PageInfo<UserCollect>(userLikeList);
        return pageData;
    }

    @Override
    public UserCollect getByCollectedTopicIdAndCollectedUserId(String collectedTopicId, String collectedUserId) {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCollect::gettTopic,collectedTopicId).eq(UserCollect::gettUser,collectedUserId);
        UserCollect userCollect = userCollectMapper.selectOne(queryWrapper);
        return userCollect;
    }

    @Override
    public void transCollectedFromRedis2DB() {
        System.out.println("开始执行定时任务——————");
        List<UserCollect> list = redisService.getCollectedDataFromRedis();
        System.out.println("从Redis中获取的数据是："+list);
        for (UserCollect collect : list) {
            UserCollect uc = getByCollectedTopicIdAndCollectedUserId(String.valueOf(collect.gettTopic()), String.valueOf(collect.gettUser()));
            if (uc == null) {
                //没有记录，直接存入
                save(collect);
            } else {
                //当存在相同的记录并且状态也是一样的时候，则不进行操作
                if (uc.getStatus().equals(collect.getStatus())){

                }else {
                    //当存在相同的记录的时候，但是状态不一样的手，则说明取消点赞，则把数据库中的记录删除
                    userCollectMapper.deleteById(uc.getId());
                    //同时将帖子列表中的点赞数减1
                    Topic topic = topicService.getTopicById(collect.gettTopic());
                    Integer collectVote = topic.getCollect()-1;
                    topic.setCollect(collectVote);
                    topicService.updateById(topic);
                }
            }
        }
    }

    @Override
    public void transCollectedCountFromRedis2DB() {
        List<CollectedCountDTO> list = redisService.getCollectedCountFromRedis();
        for (CollectedCountDTO dto : list) {
            //根据Redis中的id来查询出该帖子
            //SimpleUser user = userService.findById(dto.getId());
            //将Redis里按照String类型存储的id转为Integer类型到数据库中查找，查找出来后将该帖子的点赞量更新到记录中
            Topic topic = topicService.selectByTopicId(Integer.valueOf(dto.getId()));
            System.out.println("Redis搜索帖子，准备更新数据"+topic);
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (topic != null) {
                //将数据库中原本的数量加上Redis 中存储的
                System.out.println("原本在MySQL中存储的收藏量"+topic.getVoteCount());
                System.out.println("原本在Redis中存储的收藏量"+dto.getCount());
                Integer voteCount = topic.getVoteCount()+dto.getCount();
                System.out.println("收藏后的数量是"+voteCount);
                topic.setCollect(voteCount);
                //更新帖子的点赞数量
                topicService.updateById(topic);
            }
        }
    }

    @Override
    public boolean hasUserCollect(Integer topicId, Integer userId) {
        int result = userCollectMapper.hasUserCollect(topicId, userId);
        System.out.println("判断指定用户有没有收藏指定的帖子:"+result);
        if (result>0)return true;
        return false;
    }
}
