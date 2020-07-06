package mai.game.mapper;

import mai.game.entity.po.Announcement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-08
 */

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    Announcement getOneAnnouncement();
}
