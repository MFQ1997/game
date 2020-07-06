package mai.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import mai.game.entity.po.Article;
import mai.game.entity.po.ForumModuleClassify;
import mai.game.mapper.ForumModuleClassifyMapper;
import mai.game.service.ForumModuleClassifyService;
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
 * @since 2020-04-19
 */
@Service
public class ForumModuleClassifyServiceImpl extends ServiceImpl<ForumModuleClassifyMapper, ForumModuleClassify> implements ForumModuleClassifyService {

    @Autowired
    private ForumModuleClassifyMapper forumModuleClassifyMapper;

    @Override
    public PageInfo<ForumModuleClassify> findAllModuleClassify(int page, int row) {
        PageHelper.startPage(page,row);
        List<ForumModuleClassify> moduleClassifies = forumModuleClassifyMapper.selectList(new QueryWrapper<>());
        //用PageInfo对结果进行包装
        PageInfo<ForumModuleClassify> pageData = new PageInfo<ForumModuleClassify>(moduleClassifies);
        return pageData;
    }

    @Override
    public List<ForumModuleClassify> findAllModuleClassifyByForumModuleId(Integer forumModuleId) {
        QueryWrapper<ForumModuleClassify> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ForumModuleClassify::getModuleId,forumModuleId)
        .orderByAsc(ForumModuleClassify::getSort).orderByDesc(ForumModuleClassify::getId);
        List<ForumModuleClassify> forumModuleClassifies = forumModuleClassifyMapper.selectList(queryWrapper);
        return forumModuleClassifies;
    }


}
