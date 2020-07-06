package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.FriendUrl;
import mai.game.mapper.FriendUrlMapper;
import mai.game.service.FriendUrlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-30
 */
@Service
public class FriendUrlServiceImpl extends ServiceImpl<FriendUrlMapper, FriendUrl> implements FriendUrlService {


    @Autowired
    private FriendUrlMapper friendUrlMapper;

    @Override
    public PageInfo<FriendUrl> friendUrlPage(int page, int row) {
        PageHelper.startPage(page,row);
        List<FriendUrl> ulrs = friendUrlMapper.findAllFriendUrl();
        //用PageInfo对结果进行包装
        PageInfo<FriendUrl> pageData = new PageInfo<FriendUrl>(ulrs);
        return pageData;
    }

    @Override
    public PageInfo<FriendUrl> urlLikePage(int page, int row, FriendUrl friendUrl) {
        PageHelper.startPage(page,row);
        QueryWrapper<FriendUrl> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(!StringUtils.isEmpty(friendUrl.getName()),FriendUrl::getName,friendUrl.getName())
                .like(!StringUtils.isEmpty(friendUrl.getClassify()),FriendUrl::getClassify,friendUrl.getClassify());
        List<FriendUrl> urls = friendUrlMapper.selectList(queryWrapper);
        //用PageInfo对结果进行包装
        PageInfo<FriendUrl> pageData = new PageInfo<FriendUrl>(urls);
        return pageData;
    }
}
