package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.dto.FollowedCountDTO;
import mai.game.entity.user.UserFollow;
import mai.game.mapper.UserFollowMapper;
import mai.game.service.RedisService;
import mai.game.service.UserFollowService;
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
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public PageInfo<UserFollow> getFollowedListByUserId(String userId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserFollow::getUserId,userId);
        List<UserFollow> userLikeList = userFollowMapper.selectList(queryWrapper);
        PageInfo<UserFollow> pageData = new PageInfo<UserFollow>(userLikeList);
        return pageData;
    }

    @Override
    public PageInfo<UserFollow> getFollowedFanUserListByFollwedUserId(String followedUserId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserFollow::getFollowedUserId,followedUserId);
        List<UserFollow> userLikeList = userFollowMapper.selectList(queryWrapper);
        PageInfo<UserFollow> pageData = new PageInfo<UserFollow>(userLikeList);
        return pageData;
    }

    @Override
    public UserFollow getByFollowedUserIdAndFollowUserId(String followedUserId, String followUserId) {
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserFollow::getFollowedUserId,followedUserId).eq(UserFollow::getUserId,followUserId);
        UserFollow userFollow = userFollowMapper.selectOne(queryWrapper);
        return userFollow;
    }

    @Override
    public void transFollowedFromRedis2DB() {
        System.out.println("开始执行定时任务——————");
        List<UserFollow> list = redisService.getFollowedDataFromRedis();
        System.out.println("从Redis中获取的关注用户的数据是："+list);
        for (UserFollow follow : list) {
            UserFollow ul = getByFollowedUserIdAndFollowUserId(follow.getFollowedUserId(), follow.getUserId());
            if (ul == null) {
                //没有记录，直接存入
                save(follow);
            } else {
                //当存在相同的记录并且状态也是一样的时候，则不进行操作
                if (ul.getStatus().equals(follow.getStatus())){

                }else {
                    //当存在相同的记录的时候，但是状态不一样的手，则说明取消点赞，则把数据库中的记录删除
                    userFollowMapper.deleteById(ul.getId());
                }
            }
        }
    }

    @Override
    public void transFollowedCountFromRedis2DB() {
        List<FollowedCountDTO> list = redisService.getFollowedCountFromRedis();
        for (FollowedCountDTO dto : list) {
            //根据Redis中的id来查询出该帖子
            //SimpleUser user = userService.findById(dto.getId());
            //将Redis里按照String类型存储的id转为Integer类型到数据库中查找，查找出来后将该帖子的点赞量更新到记录中

        }
    }

    @Override
    public boolean hasUserFollow(Integer followedUserId, Integer userId) {
        int result = userFollowMapper.hasUserFollow(followedUserId, userId);
        System.out.println("判断指定用户有没有点赞指定的帖子:"+result);
        if (result>0)return true;
        return false;
    }
}
