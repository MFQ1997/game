package mai.game.mapper;

import mai.game.dto.FiveHotForumDTO;
import mai.game.dto.ForumModuleDTO;
import mai.game.dto.MasterDTO;
import mai.game.entity.po.ForumModule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-02
 */
@Mapper
public interface ForumModuleMapper extends BaseMapper<ForumModule> {

    List<ForumModule> findAllForumModule();

    List<ForumModule> findUserByLikeName(String name);

    List<ForumModule> findAllForumModuleNyAlphabet(char alphabet);

    List<ForumModule> findSixOrderByTime();

    List<ForumModule> findEightOrderByTime();

    List<ForumModule> getUserForumModuleHistoryList(List<Integer> onlyIdList);

    List<ForumModule> getForumModule(int limit);

    List<ForumModule> getHotForumModuleRanking(int limit);

    List<ForumModule> getJoinedOfUserByUserId(Integer userId);

    List<MasterDTO> getMasterByForumModuleId(int id);

    FiveHotForumDTO getForumModuleWithHotValueByForumId(Integer id);
    FiveHotForumDTO getCountDataByForumId(Integer id);

    List<ForumModuleDTO> findAllForumModuleWithMasterName();
}
