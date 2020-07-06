package mai.game.controller.admin;

import com.sun.management.OperatingSystemMXBean;
import mai.game.dto.Apply;
import mai.game.service.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.ManagementFactory;
import java.util.List;

/*
* @Description：这个是后台首页的信息
* @Date：2020
* @Since:1.0
* */

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/index")
    @RequiresPermissions("index:index")
    public String index(){
        return "admin/index";
    }

    @RequestMapping("/console")
    @RequiresPermissions("index:console")
    public String console(Model model){
        List<Apply> applyNoti = notificationService.getApplyNotificationList();
        model.addAttribute("apply",applyNoti);
        model.addAttribute("count",applyNoti.size());
        return "admin/console";
    }

    @RequestMapping("/apply/check/{notificationId}")
    @RequiresAuthentication
    public String applyCheck(@PathVariable("notificationId") Integer notificationId,Model model){
        Apply apply = notificationService.getApplyNotificationById(notificationId);
        model.addAttribute("apply",apply);
        return "/home/forum/apply-check";
    }

    @RequestMapping("/intro")
    @RequiresPermissions("index:intro")
    public String intro(Model model){
        String intro = systemConfigService.selectAllConfig().get("intro").toString();
        model.addAttribute("intro",intro);
        return "admin/system/intro";
    }

    @RequestMapping("/data")
    @RequiresPermissions("index:data")
    public String dataCount(Model model){
        Integer topicAllCount = topicService.list().size();
        Integer commentCount = commentService.list().size();
        Integer replyCount = replyService.list().size();
        Integer userCount = userService.userCount();
        model.addAttribute("topicCount",topicAllCount);
        model.addAttribute("commentCount",commentCount);
        model.addAttribute("replyCount",replyCount);
        model.addAttribute("userCount",userCount);
        return "admin/system/data";
    }
}
