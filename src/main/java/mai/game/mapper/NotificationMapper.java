package mai.game.mapper;

import mai.game.dto.Apply;
import mai.game.dto.NotificationDTO;
import mai.game.entity.po.Notification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-26
 */

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
    List<Map<String,Object>> selectByUserId(Integer userId);
    void updateAllNotificationStatus(Integer userId);
    void updateOneNotificationStatus(Integer userId, Integer notificationId);
    long countNotRead(Integer userId);


    //用户id
    boolean deleteAllByUserId(Integer id);
    //前者是接收者的id，后者是消息id
    boolean deleteByUserIdAndId(Integer id, int id1);

    List<NotificationDTO> selectWithOtherListByUserId(Integer id);

    NotificationDTO getNotifaicationWithOtherById(Integer id);

    List getApplyNotificationList();

    Apply getApplyNotificationById(Integer notificationId);

    void updateOneNotificationStatusById(Integer id);
}
