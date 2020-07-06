package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.SensitiveWord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-02
 */
public interface SensitiveWordService extends IService<SensitiveWord> {

    PageInfo<SensitiveWord> wordsPage(int page, int row);

    PageInfo<SensitiveWord> LikePage(int page, int row, SensitiveWord sensitiveWord);
}
