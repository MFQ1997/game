package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.entity.po.Collect;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.Topic;
import mai.game.entity.po.User;
import mai.game.mapper.CollectMapper;
import mai.game.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-27
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    // 查询话题被多少人收藏过
    @Override
    public List<Collect> selectByTopicId(Integer topicId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId);
        return collectMapper.selectList(wrapper);
    }

    // 查询用户是否收藏过某个话题
    @Override
    public Collect selectByTopicIdAndUserId(Integer topicId, Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId).eq(Collect::getUserId, userId);
        List<Collect> collects = collectMapper.selectList(wrapper);
        if (collects.size() > 0)
            return collects.get(0);
        return null;
    }


    // 收藏话题
    @Override
    public Collect insert(Integer topicId, User user) {
        Collect collect = new Collect();
        collect.setTopicId(topicId);
        collect.setUserId(user.getId());
        collect.setTime(new Date());
        collectMapper.insert(collect);

        // 通知
        Topic topic = topicService.selectByTopicId(topicId);
        topic.setCollect(topic.getCollect() + 1);
        topicService.update(topic, null);
        // 收藏自己的话题不发通知
        if (!user.getId().equals(topic.getUserId())) {
            notificationService.insert(user.getId(), topic.getUserId(), topicId, "COLLECT", null);
            // 发送邮件通知
            String emailTitle = "你的话题 %s 被 %s 收藏了，快去看看吧！";
            // 如果开启了websocket，就发网页通知
          /*  if (systemConfigService.selectAllConfig().get("websocket").toString().equals("1")) {
                MyWebSocket.emit(topic.getUserId(), new Message("notifications", String.format(emailTitle, topic.getTitle(),
                        user.getUsername())));
            }*/
            SimpleUser targetUser = userService.selectById(topic.getUserId());
           /* if (!StringUtils.isEmpty(targetUser.getEmail()) && targetUser.getEmailNotification()) {
                String emailContent = "<a href='%s/notifications' target='_blank'>传送门</a>";
                new Thread(() -> emailService.sendEmail(targetUser.getEmail(), String.format(emailTitle, topic.getTitle(),
                        user.getUsername()), String.format(emailContent, systemConfigService.selectAllConfig().get("base_url")
                        .toString()))).start();
            }*/
        }

        return collect;
    }

    // 删除（取消）收藏
    @Override
    public void delete(Integer topicId, Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId).eq(Collect::getUserId, userId);
        collectMapper.delete(wrapper);
        // 对话题中冗余的collectCount字段收藏数量-1
        Topic topic = topicService.selectByTopicId(topicId);
        topic.setCollect(topic.getCollect() - 1);
        topicService.update(topic, null);
    }

    // 根据话题id删除收藏记录
    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getTopicId, topicId);
        collectMapper.delete(wrapper);
    }

    // 根据用户id删除收藏记录
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getUserId, userId);
        collectMapper.delete(wrapper);
    }

    // 查询用户收藏的话题数
    @Override
    public int countByUserId(Integer userId) {
        QueryWrapper<Collect> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Collect::getUserId, userId);
        return collectMapper.selectCount(wrapper);
    }

}
