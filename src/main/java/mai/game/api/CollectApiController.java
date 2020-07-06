package mai.game.api;

/*
* @Description:这个是负责收藏帖子的API
* @Date:2020
* */

import mai.game.core.bean.Response;
import mai.game.core.exception.ApiAssert;
import mai.game.entity.po.Collect;
import mai.game.entity.po.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/collect")
public class CollectApiController extends BaseApiController{

    /*
    * @Description:这个是收藏帖子的API
    * */
/*    @PostMapping("/{topicId}")
    public Response get(@PathVariable Integer topicId) {
        User user = getApiUser();
        Collect collect = collectService.selectByTopicIdAndUserId(topicId, user.getId());
        ApiAssert.isNull(collect, "做人要知足，每人每个话题只能收藏一次哦！");
        collectService.insert(topicId, user);
        return success();
    }*/

    // 取消收藏
/*    @DeleteMapping("/{topicId}")
    public Response delete(@PathVariable Integer topicId) {
        User user = getApiUser();
        Collect collect = collectService.selectByTopicIdAndUserId(topicId, user.getId());
        ApiAssert.notNull(collect, "你都没有收藏这个话题，哪来的取消？");
        collectService.delete(topicId, user.getId());
        return success();
    }*/
}
