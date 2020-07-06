package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.dto.JoinedCountDTO;
import mai.game.entity.user.UserForumModule;
import mai.game.mapper.UserForumModuleMapper;
import mai.game.service.RedisService;
import mai.game.service.UserForumModuleService;
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
public class UserForumModuleServiceImpl extends ServiceImpl<UserForumModuleMapper, UserForumModule> implements UserForumModuleService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserForumModuleMapper userForumModuleMapper;

    @Override
    public PageInfo<UserForumModule> getJoinedListByUserId(String userId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserForumModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserForumModule::getUserId,userId);
        List<UserForumModule> userLikeList = userForumModuleMapper.selectList(queryWrapper);
        PageInfo<UserForumModule> pageData = new PageInfo<UserForumModule>(userLikeList);
        return pageData;
    }

    @Override
    public PageInfo<UserForumModule> getJoinedFanUserListByJoinedForumId(String forumId, int page, int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<UserForumModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserForumModule::getForumModuleId,forumId);
        List<UserForumModule> userLikeList = userForumModuleMapper.selectList(queryWrapper);
        PageInfo<UserForumModule> pageData = new PageInfo<UserForumModule>(userLikeList);
        return pageData;
    }

    @Override
    public UserForumModule getByJoinedForumIdAndUserId(String forumId, String userId) {
        QueryWrapper<UserForumModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserForumModule::getForumModuleId,forumId).eq(UserForumModule::getUserId,userId);
        UserForumModule userForumModuleF = userForumModuleMapper.selectOne(queryWrapper);
        return userForumModuleF;
    }

    @Override
    public void transJoinedFromRedis2DB() {
        List<UserForumModule> list = redisService.getJoinedDataFromRedis();
        for (UserForumModule join : list) {
            UserForumModule ul = getByJoinedForumIdAndUserId(String.valueOf(join.getForumModuleId()), String.valueOf(join.getUserId()));
            if (ul == null) {
                //没有记录，直接存入
                save(join);
            } else {
                //当存在相同的记录并且状态也是一样的时候，则不进行操作
                if (ul.getStatus().equals(join.getStatus())){

                }else {
                    //当存在相同的记录的时候，但是状态不一样的手，则说明取消点赞，则把数据库中的记录删除
                    userForumModuleMapper.deleteById(ul.getId());
                }
            }
        }
    }

    @Override
    public void transJoinedCountFromRedis2DB() {
        List<JoinedCountDTO> list = redisService.getJoinedCountFromRedis();
        for (JoinedCountDTO dto : list) {
            //根据Redis中的id来查询出该帖子
            //SimpleUser user = userService.findById(dto.getId());
            //将Redis里按照String类型存储的id转为Integer类型到数据库中查找，查找出来后将该帖子的点赞量更新到记录中

        }
    }

    @Override
    public boolean hasUserJoin(int forumId, Integer userId) {
        int result = userForumModuleMapper.hasUserJoin(forumId, userId);
        System.out.println("判断指定用户有没有点赞指定的帖子:"+result);
        if (result>0)return true;
        return false;
    }
}
