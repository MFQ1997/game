package mai.game.api;

import mai.game.api.BaseApiController;
import mai.game.core.bean.Response;
import mai.game.dto.FiveHotForumDTO;
import mai.game.dto.ThreePartInWeekDTO;
import mai.game.dto.TopicInWeekDTO;
import mai.game.entity.po.User;
import mai.game.service.CommentService;
import mai.game.service.ForumModuleService;
import mai.game.service.ReplyService;
import mai.game.service.TopicService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/index")
public class IndexApiController extends BaseApiController {

    @Autowired
    private ForumModuleService forumModuleService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;

    /*
    * @Description：数据统计：帖子周报
    * */
    @GetMapping("/data/seven")
    public Response theTopicNumInThisWeek(){
        List<TopicInWeekDTO> list = topicService.theTopicNumInThisWeek();
        return success(list);
    }

    /*
    * @Description:数据统计：帖子月报
    * */
    @GetMapping("/data/month")
    public Response theTopicNumInThisMonth(){
        List<TopicInWeekDTO> list = topicService.theTopicNumInThisMonth();
        return success(list);
    }

    /*
    * @Description:数据统计：帖子年报
    * */
    @GetMapping("/data/year")
    public Response theTopicNumInThisYear(){
        List<TopicInWeekDTO> list = topicService.theTopicNumInThisYear();
        return success(list);
    }

    /*
    * @Description:获取热度值前五的版块
    * */
    @GetMapping("/data/fiveHotForum")
    public Response FiveHotForumDTO(){
        List<FiveHotForumDTO> list = new ArrayList<FiveHotForumDTO>();
        List<FiveHotForumDTO> fiveHotForumDTOList = forumModuleService.getHotForumDTORanking(7);
        return success(fiveHotForumDTOList);
    }

    /*
    * @Description:获得总的发帖量
    * */
    @GetMapping("/data/three")
    public Response threePartInWeek(){
        List<ThreePartInWeekDTO> list = new ArrayList<ThreePartInWeekDTO>();
        Integer topicAllCount = topicService.list().size();
        Integer commentCount = commentService.list().size();
        Integer replyCount = replyService.list().size();
        list.add(new ThreePartInWeekDTO("发帖量",topicAllCount));
        list.add(new ThreePartInWeekDTO("评论量",commentCount));
        list.add(new ThreePartInWeekDTO("发帖量",replyCount));
        return success(list);
    }

}
