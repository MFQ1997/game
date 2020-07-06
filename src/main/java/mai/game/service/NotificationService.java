package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.dto.Apply;
import mai.game.dto.NotificationDTO;
import mai.game.entity.po.Notification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-26
 */
public interface NotificationService extends IService<Notification> {
    // 查询消息
    List<Map<String, Object>> selectByUserId(Integer userId);

    List<Notification> selectNotReadListByUserId(Integer userId);

    List<Notification> selectListByUserId(Integer id);
    List<NotificationDTO> selectWithOtherListByUserId(Integer id);
    //分页获取
    PageInfo<Notification> selectNotReadByUserId(Integer userId,int page,int row);
    //根据用户id标记为已读
    void markAllRead(Integer userId);
    void markOneRead(Integer userId,Integer notificationId);
    // 查询未读消息数量
    long countNotRead(Integer userId);
    //删除该话题下的所有通知
    void deleteByTopicId(Integer topicId);
    //删除该用户下的所有通知
    void deleteByUserId(Integer userId);
    //新增通知
    void insert(Integer userId, Integer targetUserId, Integer topicId, String action, String content);


    //通过消息接收者的id来进行其删除全部消息
    boolean deleteAllByUserId(Integer id);
    //通过用户id和消息的id来删除该消息
    boolean deleteByUserIdAndId(Integer id, int id1);


    NotificationDTO getNotifaicationWithOtherById(Integer id);

    List<Apply> getApplyNotificationList();

    Apply getApplyNotificationById(Integer notificationId);

    void markOneReadById(Integer id);
}
