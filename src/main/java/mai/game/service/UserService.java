package mai.game.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import mai.game.dto.MasterDTO;
import mai.game.entity.po.ForumModule;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.Topic;
import mai.game.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import mai.game.entity.vo.AdminUserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
public interface UserService extends IService<User> {

    //根据用户的邮箱来获取用户访问记录

    //查询用户帖子总数
    List<ForumModule> getUserForumHostory(String email);
    int userTotalTopicCountNumber(int userId);
    //查询用户获得的总评论数

    //查询用户总获的赞数

    //查询用户的总粉丝数

    //通过用户名字查询用户
    User selectByUserName(String userName);

    //通过用户名来查询出所有的信息包括权限来登录
    User findUserToLogin(String email);

    PageInfo<User> userPage(int page,int row);
    //新增用户
    User addUser(User user);
    String generateToken();
    AdminUserVO addAdminUser(AdminUserVO adminUserVO);

    //重置密码
    public boolean restPassword();
    // 根据用户token查询用户
    User selectByToken(String token);
    // 根据用户email查询用户
    User selectByEmail(String email);
    //查询出
    SimpleUser selectById(Integer id);
    // 查询前十用户积分榜
    List<User> selectTop(Integer limit);
    //根据用户id查询用户的论坛信息
    MasterDTO selectMasterByUserId(Integer userId);
    // 更新用户信息
    void update(User user);
    IPage<User> selectAll(Integer pageNo, String username);
    // 查询今天新增的话题数
    int countToday();
    // 删除用户
    void deleteUser(Integer id);
    // 删除redis缓存
    void delRedisUser(User user);
    PageInfo<User> findUserByLikeName(String name, int page, int row);
    int userCount();
    boolean isNullByEmail(String email);
    SimpleUser getUser(String username);
    //修改头像
    boolean uploadImg(Integer id,String url);
    //修改密码
    boolean updatePassword(SimpleUser user);
    //根据输入的邮件来查询出用户的信息
    SimpleUser selectSimpleByEmail(String email);

    boolean updateSimpleById(SimpleUser user);

    boolean isZan(Integer topicId, Integer userId);
    boolean isCollect(Integer topicId, Integer userId);
    boolean isFollow(Integer masterId, Integer userId);
    boolean isJoin(int forumId, Integer userId);
}
