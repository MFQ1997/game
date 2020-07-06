package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.dto.LikedCountDTO;
import mai.game.entity.po.Topic;
import mai.game.entity.user.UserLike;
import mai.game.mapper.UserLikeMapper;
import mai.game.service.RedisService;
import mai.game.service.TopicService;
import mai.game.service.UserLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mai.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 *  服务实现类：用户点赞的服务
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-28
 */
@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike> implements UserLikeService {

    @Autowired
    private UserLikeMapper userLikeMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Override
    public UserLike saveOne(UserLike userLike) {
        userLikeMapper.insert(userLike);
        return userLike;
    }

    @Override
    public List<UserLike> saveAll(List<UserLike> list) {
        Iterator<UserLike> iterator = list.iterator();
        while (iterator.hasNext()){
            UserLike userLike = iterator.next();
            userLikeMapper.insert(userLike);
        }
        return list;
    }

    /**
     * 根据被点赞帖子的id查询点赞列表（即查询都谁给这篇帖子点赞过）
     *
     * @param likedTopicId 被点赞帖子的id
     * @param PageInfo
     * @return
     */
    @Override
    public PageInfo<UserLike> getLikedUserListByLikedTopicId(String likedTopicId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserLike::getLikedTopicId,likedTopicId);
        List<UserLike> userLikeList = userLikeMapper.selectList(queryWrapper);
        PageInfo<UserLike> pageData = new PageInfo<UserLike>(userLikeList);
        return pageData;
    }

    @Override
    public PageInfo<UserLike> getLikedTopicListByLikedUserId(String likedUserId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserLike::getLikedUserId,likedUserId);
        List<UserLike> userLikeList = userLikeMapper.selectList(queryWrapper);
        PageInfo<UserLike> pageData = new PageInfo<UserLike>(userLikeList);
        return pageData;
    }

    @Override
    public UserLike getByLikedTopicIdAndLikedUserId(String likedTopicId, String likedUserId) {
        QueryWrapper<UserLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserLike::getLikedTopicId,likedTopicId).eq(UserLike::getLikedUserId,likedUserId);
        UserLike userLike = userLikeMapper.selectOne(queryWrapper);
        return userLike;
    }

    /**
     * @Description:将Redis里的点赞数据存入数据库中
     */
    @Override
    public void transLikedFromRedis2DB() {
        System.out.println("开始执行定时任务——————");
        List<UserLike> list = redisService.getLikedDataFromRedis();
        System.out.println("从Redis中获取的数据是："+list);
        for (UserLike like : list) {
            UserLike ul = getByLikedTopicIdAndLikedUserId(like.getLikedTopicId(), like.getLikedUserId());
            if (ul == null) {
                //没有记录，直接存入
                save(like);
            } else {
                //当存在相同的记录并且状态也是一样的时候，则不进行操作
                if (ul.getStatus().equals(like.getStatus())){

                }else {
                    //当存在相同的记录的时候，但是状态不一样的手，则说明取消点赞，则把数据库中的记录删除
                    userLikeMapper.deleteById(ul.getId());
                    //同时将帖子列表中的点赞数减1
                    Topic topic = topicService.getTopicById(Integer.parseInt(like.getLikedTopicId()));
                    Integer voteCount = topic.getVoteCount()-1;
                    topic.setVoteCount(voteCount);
                    topicService.updateById(topic);
                }
                //有记录，需要更新
                /*ul.setStatus(like.getStatus());
                save(ul);*/
            }
        }
    }

    /**
     * @Description:将Redis中的点赞数量数据存入数据库
     */
    @Override
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> list = redisService.getLikedCountFromRedis();
        for (LikedCountDTO dto : list) {
            //根据Redis中的id来查询出该帖子
            //SimpleUser user = userService.findById(dto.getId());
            //将Redis里按照String类型存储的id转为Integer类型到数据库中查找，查找出来后将该帖子的点赞量更新到记录中
            Topic topic = topicService.selectByTopicId(Integer.valueOf(dto.getId()));
            System.out.println("Redis搜索帖子，准备更新数据"+topic);
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (topic != null) {
                //将数据库中原本的数量加上Redis 中存储的
                System.out.println("原本在MySQL中存储的点赞量"+topic.getVoteCount());
                System.out.println("原本在Redis中存储的点赞量"+dto.getCount());
                Integer voteCount = topic.getVoteCount()+dto.getCount();
                System.out.println("点赞后的数量是"+voteCount);
                topic.setVoteCount(voteCount);
                //更新帖子的点赞数量
                topicService.updateById(topic);
            }
        }
    }

    /*
    * @DEscription：判断指定用户有没有点赞指定的帖子
    * */
    @Override
    public boolean hasUserZan(Integer topicId, Integer userId) {
        int result = userLikeMapper.hasUserZan(topicId, userId);
        System.out.println("判断指定用户有没有点赞指定的帖子:"+result);
        if (result>0)return true;
        return false;
    }
}
