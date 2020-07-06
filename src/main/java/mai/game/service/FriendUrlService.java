package mai.game.service;

import com.github.pagehelper.PageInfo;
import mai.game.entity.po.FriendUrl;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-30
 */
public interface FriendUrlService extends IService<FriendUrl> {

    PageInfo<FriendUrl> friendUrlPage(int page, int row);

    PageInfo<FriendUrl> urlLikePage(int page, int row, FriendUrl friendUrl);
}
