package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Announcement;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-08
 */
public interface AnnouncementService extends IService<Announcement> {

    PageInfo<Announcement> announcementPage(int page, int row);

    Announcement getOneAnnouncement();
}
