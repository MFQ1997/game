package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Announcement;
import mai.game.mapper.AnnouncementMapper;
import mai.game.service.AnnouncementService;
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
 * @since 2020-05-08
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public PageInfo<Announcement> announcementPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<Announcement> list = announcementMapper.selectList(null);
        PageInfo<Announcement> pageData = new PageInfo<Announcement>(list);
        return pageData;
    }

    @Override
    public Announcement getOneAnnouncement() {
        return announcementMapper.getOneAnnouncement();
    }
}
