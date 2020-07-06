package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.dto.FiveHotForumDTO;
import mai.game.dto.ForumModuleDTO;
import mai.game.dto.MasterDTO;
import mai.game.entity.po.ForumModule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-02
 */
public interface ForumModuleService extends IService<ForumModule> {

    PageInfo<ForumModule> findAllModule(int page, int row);
    PageInfo<ForumModule> findForumModuleByLikeName(String name, int page, int row);
    PageInfo<ForumModule> moduleLikePage(int page, int row, ForumModule forumModule);

    /*
    * 根据字母获取
    * */
    List<ForumModule> findAllModuleByAlphabet(char alphabet);

    List<ForumModule> findSixOrderByTime();
    List<ForumModule> findEightOrderByTime();

    List<ForumModule> getUserForumModuleHistoryList(List<Integer> onlyIdList);
    List<ForumModule> getForumModule(int limit);

    List<ForumModule> hotForumModuleRanking();    //计算热度值

    ForumModule getForumModuleById(int id);
    List<ForumModule> getHotForumModuleRanking(int limit);

    //获取指定用户入住的板块
    PageInfo<ForumModule> getJoinedOfUserByUserId(Integer id, int page, int limit);

    void setApplyMaster(Integer forumId, Integer userId);
    //根据板块Id获取版主列表
    List<MasterDTO> getMasterByForumModuleId(int id);

    List<FiveHotForumDTO> getHotForumDTORanking(int i);

    FiveHotForumDTO getCountDataByForumId(int id);

    PageInfo<ForumModuleDTO> findAllModuleWithMasterName(int page, int row);
}
