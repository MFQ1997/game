package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.dto.Apply;
import mai.game.dto.NotificationDTO;
import mai.game.entity.po.Notification;
import mai.game.mapper.NotificationMapper;
import mai.game.service.NotificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-26
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    // 查询消息
    @Override
    public List<Map<String, Object>> selectByUserId(Integer userId) {
        List<Map<String, Object>> notifications = notificationMapper.selectByUserId(userId);
        return notifications;
    }

    @Override
    public List<Notification> selectNotReadListByUserId(Integer userId) {
        QueryWrapper<Notification> notificationQueryWrapper = new QueryWrapper<>();
        notificationQueryWrapper.lambda().eq(Notification::getTargetUser,userId).eq(Notification::getReadStatus,0);
        List<Notification> notificationList = notificationMapper.selectList(notificationQueryWrapper);
        return notificationList;
    }

    @Override
    public List<Notification> selectListByUserId(Integer id) {
        QueryWrapper<Notification> notificationQueryWrapper = new QueryWrapper<>();
        notificationQueryWrapper.lambda().eq(Notification::getTargetUser,id);
        List<Notification> notificationList = notificationMapper.selectList(notificationQueryWrapper);
        return notificationList;
    }

    @Override
    public List<NotificationDTO> selectWithOtherListByUserId(Integer id) {
        return notificationMapper.selectWithOtherListByUserId(id);
    }

    //通过用户id来获取其未读消息
    @Override
    public PageInfo<Notification> selectNotReadByUserId(Integer userId,int page,int row) {
        PageHelper.startPage(page,row);
        QueryWrapper<Notification> notificationQueryWrapper = new QueryWrapper<>();
        notificationQueryWrapper.lambda().eq(Notification::getTargetUser,userId).eq(Notification::getReadStatus,0);
        List<Notification> notificationList = notificationMapper.selectList(notificationQueryWrapper);
        //用PageInfo对结果进行包装
        PageInfo<Notification> pageData = new PageInfo<Notification>(notificationList);
        return pageData;
    }


    /*
    * @Description:标记为已读
    * */
    @Override
    public void markAllRead(Integer userId) {
        notificationMapper.updateAllNotificationStatus(userId);
    }

    @Override
    public void markOneRead(Integer userId, Integer notificationId) {
        notificationMapper.updateOneNotificationStatus(userId,notificationId);
    }


    // 查询未读消息数量
    @Override
    public long countNotRead(Integer userId) {
        return notificationMapper.countNotRead(userId);
    }

    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Notification::getTopicId, topicId);
        notificationMapper.delete(wrapper);
    }

    /*
    * @Description:根据用户id来删除已读消息
    * */
    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Notification::getTargetUser, userId).or().eq(Notification::getUserId, userId);
        notificationMapper.delete(wrapper);
    }

    /*
    * @Description:创建通知
    * */
    @Override
    public void insert(Integer userId, Integer targetUserId, Integer topicId, String action, String content) {
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setContent(content);
        notification.setUserId(userId);
        notification.setTargetUser(targetUserId);
        notification.setTopicId(topicId);
        notification.setInTime(new Date());
        notification.setReadStatus(0);
        System.out.println("通知是："+notification);
        notificationMapper.insert(notification);
    }

    @Override
    public boolean deleteAllByUserId(Integer id) {
        return notificationMapper.deleteAllByUserId(id);
    }

    @Override
    public boolean deleteByUserIdAndId(Integer id, int id1) {
        return notificationMapper.deleteByUserIdAndId(id,id1);
    }

    @Override
    public NotificationDTO getNotifaicationWithOtherById(Integer id) {
        return notificationMapper.getNotifaicationWithOtherById(id);
    }

    @Override
    public List<Apply> getApplyNotificationList() {
        List<Apply> applyNotificationList = notificationMapper.getApplyNotificationList();
        return applyNotificationList;
    }

    @Override
    public Apply getApplyNotificationById(Integer notificationId) {
        return notificationMapper.getApplyNotificationById(notificationId);
    }

    @Override
    public void markOneReadById(Integer id) {
        notificationMapper.updateOneNotificationStatusById(id);
    }
}
