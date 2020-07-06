package mai.game.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.core.util.PasswordUtil;
import mai.game.dto.MasterDTO;
import mai.game.entity.po.*;
import mai.game.entity.vo.AdminUserVO;
import mai.game.mapper.TopicMapper;
import mai.game.mapper.UserMapper;
import mai.game.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.util.ByteSource;
import org.assertj.core.api.Fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private SystemLogService systemLogService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    private TopicService topicService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private ForumModuleService forumModuleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserLikeService userLikeService;
    @Autowired
    private UserCollectService userCollectService;
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private UserForumModuleService userJoinService;


    @Override
    public List<ForumModule> getUserForumHostory(String email) {
        //根据用户的邮箱账号来从日志表中获取访问板块的历史记录
        List<Integer> userForumModuleHistoryIdList = systemLogService.getUserForumHostoryByEmail(email);
        //将得到的list进行去重操作,得到一个没有重复元素的List
        List<Integer> onlyIdList = userForumModuleHistoryIdList.stream().distinct().collect(Collectors.toList());
        List<ForumModule> userForumModuleHistoryList = forumModuleService.getUserForumModuleHistoryList(onlyIdList);
        return userForumModuleHistoryList;
    }

    @Override
    public int userTotalTopicCountNumber(int userId) {
        return topicService.userTotalTopicCountNumber(userId);
    }

    @Override
    public User selectByUserName(String userName) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUserName,userName);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User findUserToLogin(String email) {
        return userMapper.findUserToLogin(email);
    }

    @Override
    public PageInfo<User> userPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<User> users = userMapper.findAllRole();
        //用PageInfo对结果进行包装
        PageInfo<User> pageData = new PageInfo<User>(users);
        return pageData;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    // 递归生成token，防止token重复
    // 理论上uuid生成的token是不可能重复的
    // 加个逻辑放心 : )
    @Override
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        User user = this.selectByToken(token);
        if (user != null) {
            return this.generateToken();
        }
        return token;
    }

    @Override
    @Transactional
    public AdminUserVO addAdminUser(AdminUserVO adminUserVO) {
        User user = new User();
        user.setEmail(adminUserVO.getEmail());
        user.setPassword(adminUserVO.getPassword());
        user.setStatus(adminUserVO.getStatus());
        //设置加密加盐之后的新密码给user
        PasswordUtil passwordHelper = new PasswordUtil();
        passwordHelper.encryptPassword(user);
        String salt = ByteSource.Util.bytes(user.getEmail()).toString();
        //将新增用户的时候产生的盐值添加到数据库中
        user.setSalt(salt);
        //插入到数据库中
        userMapper.insert(user);
        int userId = user.getId();
        System.out.println("用户的id是"+userId);
        Iterator<Integer> iter = adminUserVO.getRoleList().iterator();
        while (iter.hasNext()) {
            Integer id = (Integer) iter.next();
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(id);
            userRoleService.insert(userRole);
            //返回插入成功的信息
        }
        return adminUserVO;
    }

    @Override
    public boolean restPassword() {
        return false;
    }


    @Override
    public User selectByToken(String token) {
       /* QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getToken, token);
        return userMapper.selectOne(wrapper);*/
       return null;
    }

    @Override
    public User selectByEmail(String email) {
        return null;
    }

    @Override
    public SimpleUser selectById(Integer id) {
        return userMapper.selectBySimpleUserId(id);
    }

    @Override
    public List<User> selectTop(Integer limit) {
        return null;
    }

    /*
    * @Description:根据用户id查询出其发帖数，粉丝数，点赞数、回复数等等
    * */
    @Override
    public MasterDTO selectMasterByUserId(Integer userId) {
        return userMapper.selectMasterByUserId(userId);
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    public IPage<User> selectAll(Integer pageNo, String username) {
        return null;
    }

    @Override
    public int countToday() {
        return 0;
    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public void delRedisUser(User user) {

    }

    @Override
    public PageInfo<User> findUserByLikeName(String name, int page, int row) {
        PageHelper.startPage(page,row);
        List<User> users = userMapper.findUserByLikeName(name);
        //用PageInfo对结果进行包装
        PageInfo<User> pageData = new PageInfo<User>(users);
        return pageData;
    }

    @Override
    public int userCount() {
        return userMapper.userCount();
    }

    /*
    * @Description:当数据库返回的数据不为0的时候，返回false代表不为空
    * */
    @Override
    public boolean isNullByEmail(String email) {
        SimpleUser nullByEmail = userMapper.isNullByEmail(email);
        if(nullByEmail==null)
            return true;
        return false;
    }

    /*
    * @Description：此时的username就是email
    * */
    @Override
    public SimpleUser getUser(String username) {
        return userMapper.findSimpleUserByEmail(username);
    }

    @Override
    public boolean uploadImg(Integer id,String url) {
        User user = new User();user.setId(id);user.setImg(url);
        int i = userMapper.updateById(user);
        if (i>0)
            return true;
        return false;
    }

    @Override
    public boolean updatePassword(SimpleUser user) {
        return userMapper.updatePassword(user);
    }

    /*
    * @Description:根据用户输入的邮箱来查找出用户的基本信息（即不包括角色、权限等信息）
    * */
    @Override
    public SimpleUser selectSimpleByEmail(String email) {
        return userMapper.findSimpleUserByEmail(email);
    }

    @Override
    public boolean updateSimpleById(SimpleUser user) {
        return userMapper.updateSimpleById(user);
    }

    @Override
    public boolean isZan(Integer topicId, Integer userId) {
        Integer redisResult = redisService.hasUserZan(topicId,userId);
        if (redisResult.equals(1)) return true;
        if (redisResult.equals(-1)) return false;
        else return userLikeService.hasUserZan(topicId,userId);

    }

    @Override
    public boolean isCollect(Integer topicId, Integer userId) {
        Integer redisResult = redisService.hasUserCollect(topicId,userId);
        if (redisResult.equals(1)) return true;
        if (redisResult.equals(-1)) return false;
        else return userCollectService.hasUserCollect(topicId,userId);
    }

    @Override
    public boolean isFollow(Integer masterId, Integer userId) {
        Integer redisResult = redisService.hasUserFollow(masterId,userId);
        if (redisResult.equals(1)) return true;    //代表Redis存在用户存在点赞记录
        if (redisResult.equals(-1)) return false;  //此时MySQL存在用户点赞的记录，但是此时Redis存在取消点赞的记录，此时系统按照REdis的记录为准
        else return userFollowService.hasUserFollow(masterId,userId);
    }

    @Override
    public boolean isJoin(int forumId, Integer userId) {
        Integer redisResult = redisService.hasUserJoin(forumId,userId);
        if (redisResult.equals(1))return true;
        if (redisResult.equals(-1)) return false;
        else return userJoinService.hasUserJoin(forumId,userId);
    }

}
