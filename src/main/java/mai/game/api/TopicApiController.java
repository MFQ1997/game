package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.exception.ApiAssert;
import mai.game.entity.po.Collect;
import mai.game.entity.po.Topic;
import mai.game.entity.po.User;
import mai.game.entity.vo.TopicVO;
import mai.game.service.CollectService;
import mai.game.service.RedisService;
import mai.game.service.TopicService;
import mai.game.service.UserService;
import net.sf.jsqlparser.statement.select.Top;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/topic")
public class TopicApiController extends BaseApiController{

    @Autowired
    private TopicService topicService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private UserService userService;

    /*
    * @Description:这个是分页获取到所有的帖子
    * */
    @GetMapping
    @Log(value="获取所有帖子项")
    public Response<PageInfo<Topic>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Topic> topicsPage = topicService.topicPage(page,row);
        int count = topicService.list().size();
        return success("success",0,count,topicsPage);
    }

    /*
     * @Description:使用layui流加载
     */
    @GetMapping("/roll/{id}")
    public Response<PageInfo<Topic>> get_info_list(@PathVariable("id") int id,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit) {
        //分页查询，需要参数code（要为0，不然数据表格数据显示不出）,msg（返回的消息），data(表格显示的数据)，totals(查询到数据的总记录数)，
        PageInfo<Topic> topicsPage = topicService.listProjectByPageAndLimit(id,page,limit);
        //返回的总记录数
        int count = topicService.list().size();
        return success("success",0,count,topicsPage);
    }

    @GetMapping("/roll/{forumId}/{classifyId}")
    public Response<PageInfo<Topic>> getTopicByForumIdWithClassifyId(@PathVariable("forumId") int forumId,@PathVariable("classifyId") int classifyId,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit) {
        System.out.println("进入论坛的分类的帖子获取操作：板块id是:"+forumId+"分类Id是："+classifyId);
        //分页查询，需要参数code（要为0，不然数据表格数据显示不出）,msg（返回的消息），data(表格显示的数据)，totals(查询到数据的总记录数)，
        PageInfo<Topic> topicsPage = topicService.listProjectByPageAndLimit(forumId,classifyId,page,limit);
        //返回的总记录数
        System.out.println("查询分类的帖子信息是:"+topicsPage);
        int count = topicService.getCountByForumIdAndClassifyId(forumId,classifyId);
        System.out.println("板块分类查询的数据总量是："+count);
        return success("success",0,count,topicsPage);
    }

    /*
     * @Description:根据名字进行模糊查询
     * @Param:name(String)
     * @Date:2019
     * */
    @GetMapping("/name/{name}")
    @ResponseBody
    @Log(value = "模糊查询帖子（主题）")
    //@RequiresPermissions("user:name")
    public Response<PageInfo<Topic>> findTopicByLikeName(@PathVariable("name") String name,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<Topic> pageResult = topicService.findTopicByLikeName(name,page,row);
        return success(0,pageResult);
    }

    @GetMapping("/id/{id}")
    public Response<Topic> getTopicById(@PathVariable("id") int id){
        Topic topic = topicService.getById(id);
        return success(0,topic);
    }

    /*
    * @Description:这个是新增帖子的操作
    * */
    @PostMapping
    @Log(value="发帖子")
    public Response add(@RequestBody TopicVO topicVO){
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        topicService.postTopic(topicVO,user);
        return success("发布成功",0,"success");
       /* return fail("发布失败",1,"fail");*/
    }

    /*
     * @Description:这个是修改帖子的操作
     * */
    @PutMapping
    @Log(value="更新帖子项")
    public Response update(@RequestBody Topic topic){
        boolean b = topicService.updateById(topic);
        if (b)
            return success(0,"success");
        return fail(1,"fail");
    }

    /*
     * @Description:这个是删除帖子的操作
     * */
    @DeleteMapping("/{id}")
    @Log(value="删除帖子项")
    public Response delete(@PathVariable("id") int id){
        boolean b = topicService.removeById(id);
        if (b)
            return success(0,"success");
        return fail(1,"fail");
    }

    /*
    * @Description:根据用户Id来获取该用户的帖子
    * */
    @PostMapping("/user/{userId}")
    public Response<PageInfo<Topic>> getTopicOfUserByUserId(@PathVariable("userId") Integer userId,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit){
        System.out.println("获取帖子的用户是："+userId);
        PageInfo<Topic> list = topicService.getTopicByUserId(userId,page,limit);
        return success(list);
    }

    @GetMapping("/user/collect")
    @RequiresAuthentication
    public Response<PageInfo<Topic>> getCollectedOfUserByUserId(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        PageInfo<Topic> list = topicService.getCollectedOfUserByUserId(userBean.getId(),page,limit);
        return success(list);
    }



    /*
    * @Description:这个是根据分类id来获取该分类下的帖子（话题）
    * */
    @RequestMapping("/classify/{classifyId}")
    public Response<PageInfo<Topic>> getTopicByClassify(@PathVariable("classifyId") int classifyId,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit) {
        //分页查询，需要参数code（要为0，不然数据表格数据显示不出）,msg（返回的消息），data(表格显示的数据)，totals(查询到数据的总记录数)，
        PageInfo<Topic> topicsPage = topicService.getTopicByClassifyByPageAndLimit(classifyId,page,limit);
        //返回的总记录数
        int count = topicService.list().size();
        return success("success",0,count,topicsPage);
    }

    /*
    * @Description:置顶帖子操作
    * */
    @PutMapping("/isTop/{id}/{isTop}")
    @Log(value = "板块状态")
    @RequiresPermissions("topic:top")
    public Response updateForumModuleisTop(@PathVariable("id") Integer id, @PathVariable("isTop") Integer status){
        System.out.println("进入置顶操作");
        Topic topic = new Topic();topic.setId(id);topic.setIsTop(status);
        boolean b = topicService.updateById(topic);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);
    }
    /*
     * @Description:设置为精华帖
     * */
    @PutMapping("/isGood/{id}/{isGood}")
    @Log(value = "板块状态")
    @RequiresPermissions("topic:good")
    public Response updateForumModuleisGood(@PathVariable("id") Integer id, @PathVariable("isGood") Integer status){
        Topic topic = new Topic();topic.setId(id);topic.setIsGood(status);
        boolean b = topicService.updateById(topic);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);
    }

    // 收藏话题
    @PostMapping("/{topicId}")
    public Response get(@PathVariable Integer topicId) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return fail("请先登陆",1,null);
        }
        Collect collect = collectService.selectByTopicIdAndUserId(topicId, user.getId());
        /*ApiAssert.isNull(collect, "做人要知足，每人每个话题只能收藏一次哦！");*/
        collectService.insert(topicId, user);
        return success("收藏成功",0,null);
    }

    // 取消收藏
    @DeleteMapping("/{topicId}")
    public Response delete(@PathVariable Integer topicId) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        Collect collect = collectService.selectByTopicIdAndUserId(topicId, user.getId());
        ApiAssert.notNull(collect, "你都没有收藏这个话题，哪来的取消？");
        collectService.delete(topicId, user.getId());
        return success("取消收藏成功",0,null);
    }





    /*
    * @Description:关闭评论操作
    * */




    /*
    * @Description：点赞评论
    * */
    @GetMapping("/{id}/vote")
    public Response vote(@PathVariable Integer id, HttpSession session) {
        User user = getApiUser();
        Topic topic = topicService.getById(id);
        ApiAssert.notNull(topic, "这个话题可能已经被删除了");
        ApiAssert.notTrue(topic.getUserId().equals(user.getId()), "给自己话题点赞，脸皮真厚！！");
        int voteCount = topicService.vote(topic, getApiUser(), session);
        return success(0,voteCount);
    }

    /*
    * @Description：算出热度帖子(不区分板块)
    * */
    /*public Response hotTopicRanking(){
        List<Topic> hotTopicRankingList = topicService.hotTopicRanking();
        return success(null);
    }*/

    /*
    * @Description：算出某板块下的热度帖子
    * */
    public Response hotTopicRankingInForumModule(){

        return success(null);
    }




}
