package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.ForumModuleClassify;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-19
 */
public interface ForumModuleClassifyService extends IService<ForumModuleClassify> {

    //分页获取分类信息
    PageInfo<ForumModuleClassify> findAllModuleClassify(int page, int row);
    //根据板块id来获取该板块下的分类信息
    List<ForumModuleClassify> findAllModuleClassifyByForumModuleId(Integer forumModuleId);
}
