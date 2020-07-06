package mai.game.controller.home;

/*
* @Description:这个是论坛的前台控制器
* */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.core.annotation.Log;
import mai.game.dto.FiveHotForumDTO;
import mai.game.dto.MasterDTO;
import mai.game.entity.po.*;
import mai.game.entity.vo.CommentVO;
import mai.game.entity.vo.ReplyVO;
import mai.game.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@Controller
@RequestMapping("/bbs")
@Validated
public class BbsController {

    @Autowired
    private ForumModuleService forumModuleService;
    @Autowired
    private ForumModuleClassifyService forumModuleClassifyService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private ClassifyService classifyService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private FriendUrlService friendUrlService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SlideService slideService;

    @Autowired
    private AnnouncementService announcementService;

    /*
    * @Description:这个是论坛的首页页面
    * */
    @RequestMapping("/")
    public String index(Model model){

        /*
        * 轮播图
        * */
        List<Slide> slideList = slideService.findSlide();
        model.addAttribute("slide",slideList);
        /*
        * 用户访问记录:当用户登录的时候才返回这些内容，shiro主体存在的时候
        * */
        try {
            String userEmail = SecurityUtils.getSubject().getPrincipal().toString() ;
            List<ForumModule> userHistoryList = userService.getUserForumHostory(userEmail);
            model.addAttribute("userHistory",userHistoryList);
        }catch (NullPointerException e){ }
        /*
        * 新出板块
        * */
        List<ForumModule> sixForumOrderByTime = forumModuleService.findSixOrderByTime();
        List<ForumModule> eightForumOrderByTime = forumModuleService.findEightOrderByTime();
        model.addAttribute("sixForum",sixForumOrderByTime);
        model.addAttribute("eightForum",eightForumOrderByTime);
        /*
         * 热门板块
         * */
        List<ForumModule> forumModuleHotList = forumModuleService.getHotForumModuleRanking(6);
        model.addAttribute("hotModuleRanking",forumModuleHotList);
        /*
         * 友情链接
         * */
        QueryWrapper<FriendUrl> urlQueryWrapper = new QueryWrapper<>();
        urlQueryWrapper.lambda().orderByDesc(FriendUrl::getId).eq(FriendUrl::getClassify,1);
        List<FriendUrl> url = friendUrlService.list(urlQueryWrapper);
        model.addAttribute("url",url);

        /*
        * @Description：热门帖子
        * */
        List<Topic> hotTopicRanking = topicService.getHotTopicRanking(10);
        model.addAttribute("hotTopicRanking",hotTopicRanking);

        /*
        * @Desription：系统公告
        * */
        Announcement announcement = announcementService.getOneAnnouncement();
        model.addAttribute("announcement",announcement);
        return "/home/forum/index";
    }

    /*
    * @Description:这个是论坛板块的列表页面
    * */
    @RequestMapping("/mlist")
    public String list(Model model){
        List<ForumModule> forumModuleList = forumModuleService.list();
        model.addAttribute("forumModuleList",forumModuleList);
        return "/home/forum/list";
    }

    /*
    * 根据字母查询板块
    * */
    @GetMapping("/mlist/find/{alphabet}")
    public String findByAlphabetList(@Pattern (regexp = "^[A-Z]+$",message = "参数格式错误")@PathVariable("alphabet")String alphabet, Model model){
        char alphabet1 = alphabet.charAt(0);
        System.out.println("字母是："+alphabet1);
        List<ForumModule> list = forumModuleService.findAllModuleByAlphabet(alphabet1);
        int size = list.size();
        model.addAttribute("forumModuleList",list);
        model.addAttribute("size",size);
        return "/home/forum/list";
    }

    /*
    * 前台模糊出来的帖子
    * */
    @PostMapping("/topic/find/")
    public String findLikeTopicByTitle(@Pattern(regexp = "^[a-zA-Z0-9\u4E00-\u9FA5]+$", message = "xxx：输入内容格式不规范")@RequestParam("title") String title, Model model){
        //返回关键字
        model.addAttribute("title",title);
        List<Topic> topicList = topicService.findTopicListByTitle(title);
        int size = topicList.size();
        model.addAttribute("size",size);
        model.addAttribute("topicList",topicList);
        return "/home/forum/topic-find-list";
    }

    /*
     * @Description:这个是单个板块的帖子（主题）列表页面
     * */
    @RequestMapping("/one/{id}")
    @Log(value = "访问板块")
    public String one(@PathVariable("id") int id, Model model){
        ForumModule forum = forumModuleService.getForumModuleById(id);
        model.addAttribute("forum",forum);
        //获取板块下的分类信息
        List<ForumModuleClassify> allModuleClassifyByForumModuleId = forumModuleClassifyService.findAllModuleClassifyByForumModuleId(forum.getId());
        model.addAttribute("classify",allModuleClassifyByForumModuleId);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            try {
                String userEmail = subject.getPrincipal().toString();
                User userBean = userService.findUserToLogin(userEmail);
                //判断是否入驻
                boolean isJoin = userService.isJoin(id,userBean.getId());
                if (isJoin) model.addAttribute("isJoin",1);
                else model.addAttribute("isJoin",0);
            }catch (NullPointerException e){
                System.out.println("用户没登录");
            }
        }else {
            model.addAttribute("isJoin",0);
        }

        //获取版主信息
        List<MasterDTO> masterDTO = forumModuleService.getMasterByForumModuleId(id);
        if (masterDTO.isEmpty()){
            model.addAttribute("masterSize",0);
        }else {
            model.addAttribute("masterSize",masterDTO.size());
        }
        model.addAttribute("master",masterDTO);
        FiveHotForumDTO totalData = forumModuleService.getCountDataByForumId(id);
        model.addAttribute("data",totalData);
        return "/home/forum/one";
    }



    /*
     * @Description:这个是论坛板块的帖子（主题）的详情页面
     * */
    @RequestMapping("/one/topic/{id}")
    public String detail(@PathVariable("id") int id,Model model){
        //获取帖子（主题的信息）
        Topic topic = topicService.getTopicById(id);
        String likedCountFromRedisByTopicId = redisService.getLikedCountFromRedisByTopicId(topic.getId());
        if (likedCountFromRedisByTopicId !=null&&likedCountFromRedisByTopicId.length()!=0){
            Integer redisLikeCount = Integer.parseInt(likedCountFromRedisByTopicId);
            System.out.println("Redis中的数据是："+redisLikeCount);
            int a=redisLikeCount.intValue();
            if (a>0)
                topic.setVoteCount(topic.getVoteCount()+a);
        }
        model.addAttribute("topic",topic);
        //获得该帖子下的所有标签
        List<Tag> tagList = tagService.selectByTopicId(topic.getId());
        model.addAttribute("tagList",tagList);
        //查询帖子的作者
        SimpleUser master = userService.selectById(topic.getUserId());
        MasterDTO masterDTO = userService.selectMasterByUserId(topic.getUserId());
        model.addAttribute("master",masterDTO);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            try {
                String userEmail = subject.getPrincipal().toString();
                User userBean = userService.findUserToLogin(userEmail);
                model.addAttribute("userId",userBean.getId());
                //判断是否点赞
                boolean isZan = userService.isZan(topic.getId(),userBean.getId());
                if (isZan) model.addAttribute("isZan",1);
                else model.addAttribute("isZan",0);
                //判断是否收藏
                boolean isCollect = userService.isCollect(topic.getId(),userBean.getId());
                if (isCollect) model.addAttribute("isCollect",1);
                else model.addAttribute("isCollect",0);
                //判断是否关注
                boolean isFollow = userService.isFollow(topic.getUserId(),userBean.getId());
                if (isFollow) model.addAttribute("isFollow",1);
                else model.addAttribute("isFollow",0);
                //判断登录者是否是本人
                if (userBean.getId().equals(topic.getUserId()))model.addAttribute("isAuthor",1);
                else model.addAttribute("isAuthor",0);
            }catch (NullPointerException e){
                System.out.println("用户没登录");
            }
        }else {
            model.addAttribute("isZan",0);
            model.addAttribute("isCollect",0);
            model.addAttribute("isFollow",0);
            model.addAttribute("isAuthor",0);
        }
        //查询帖子评论

        List<CommentVO> commentList = commentService.getCommentByTopicId(id);
        model.addAttribute("commentList",commentList);
        //查询回复
        List<ReplyVO> replyList = replyService.getReplyWithUserNameAndPhotoList(id);
        model.addAttribute("replyList",replyList);
        //获取评论以及回复的相关信息
        return "/home/forum/detail";
    }

    /*
    * @Description:这个是论坛板块的评分页面
    * */
    @RequestMapping("/forum/grade")
    public String grade(Model model){
        List<ForumModule> forumModuleList = forumModuleService.list();
        model.addAttribute("forumModuleList",forumModuleList);
        return "/home/forum/grade";
    }


    /*
    * @Description:这个是申请版主的页面
    * */
    @RequestMapping("/forum/apply/{forumId}")
    public String apply(@PathVariable("forumId") Integer forumId,Model model){
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return "redirect:/my/login";
        }
        ForumModule forumModule = forumModuleService.getById(forumId);
        model.addAttribute("forum",forumModule);
        System.out.println("进入入驻板块接口");
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        SimpleUser user = userService.getUser(userEmail);
        model.addAttribute("user",user);
        return "/home/forum/apply";
    }

    /*
    * @Description:发布回复
    * */
    @PostMapping("/topic/reply")
    public String reply(@RequestParam("topicId") int topicId,@RequestParam("toUserId") int toUserId,
                        @RequestParam("commentId")int commentId,@RequestParam("replyId")int replyId,
                        @RequestParam("replyType")int replyType,@RequestParam("content")String content){
        //当用户没登录的时候，转发到登录界面
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return "redirect:/my/login";
        }
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        int userId = user.getId();
        boolean result = replyService.postReply(toUserId,commentId,replyId,replyType,content,userId);
        //处理完之后就转发回原来界面
        return "redirect:/bbs/one/topic/"+topicId;
    }

    /*
     * @Description:帖子版主用户主页
     * @Date:2020
     * */
    @RequestMapping("/home/{userId}")
    public String home(@PathVariable("userId") int userId,Model model){
        //获取当前用户信息
        SimpleUser user = userService.selectById(userId);
        model.addAttribute("master",user);

        List<Topic> userTopicList = topicService.getTopicListByUserId(userId);
        System.out.println("该用户查询到帖子是"+userTopicList);
        model.addAttribute("topic",userTopicList);
        return "/home/forum/home";
    }

}
