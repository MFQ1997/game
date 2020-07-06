package mai.game.controller.home;

import mai.game.dto.NotificationDTO;
import mai.game.entity.po.Notification;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.Topic;
import mai.game.entity.po.User;
import mai.game.service.NotificationService;
import mai.game.service.TopicService;
import mai.game.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/my")
public class myController {

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/login")
    public String login(){
        return "/home/user/login";
    }

    @RequestMapping("/register")
    public String register(){
        return "/home/user/register";
    }

    @RequestMapping("/mes")
    @RequiresAuthentication
    public String message(Model model){
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return "redirect:/my/login";
        }
        return "/home/user/message";
    }

    @RequestMapping("/forget")
    public String forget(){
        return "/home/user/forget";
    }

    /*
     * @Description:前台用户主页
     * @Date:2020
     * */
    @RequestMapping("/home")
    @RequiresAuthentication
    public String home(Model model){
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return "redirect:/my/login";
        }else{
            Subject subject = SecurityUtils.getSubject();
            String userEmail = subject.getPrincipal().toString();
            SimpleUser user = userService.getUser(userEmail);
            System.out.println("访问主页的用户是"+user);
            model.addAttribute("me",user);
        }
        return "/home/user/my";
    }

    @RequestMapping("/user/message/detail/{id}")
    public String messageDetail(@PathVariable("id") Integer id,Model model){
        //Notification notification = notificationService.getById(id);
        NotificationDTO notificationDTO = notificationService.getNotifaicationWithOtherById(id);
        model.addAttribute("message",notificationDTO);
        return "/home/user/message-detail";
    }
    /*
    * @Description：这是前台用户的消息也
    * */
    @GetMapping("/user/message")
    public String messeage(Model model){
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return "redirect:/my/login";
        }
        //List<Notification> userNotificationList = notificationService.list();
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);

        //List<Notification> message = notificationService.selectListByUserId(userBean.getId());
        List<NotificationDTO> message = notificationService.selectWithOtherListByUserId(userBean.getId());
        model.addAttribute("messageSize",message.size());
        model.addAttribute("message",message);
        model.addAttribute("section", "message");
        return "/home/user/message";
    }

    @GetMapping("/user/p/{action}")
    public String p(@PathVariable(name = "action") String action, Model model){
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return "redirect:/my/login";
        }
        if("myPosts".equals(action)){
            model.addAttribute("section", "myPosts");
            model.addAttribute("sectionName", "我的帖子");
            model.addAttribute("navtype", "communitynav");
        }
        if("likes".equals(action)){
            model.addAttribute("section", "likes");
            model.addAttribute("sectionName", "我的收藏");
            model.addAttribute("navtype", "communitynav");
        }

        return "/home/user/p";
    }

    @GetMapping("/user/set/{action}")
    public String getSetPage(HttpServletRequest request,
                             @PathVariable(name = "action") String action,
                             Model model) {
        if((User) SecurityUtils.getSubject().getSession().getAttribute("user") ==null){
            return "redirect:/my/login";
        }
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");

        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        System.out.println("用户中心的信息是："+userBean);
        model.addAttribute("user",userBean);
        model.addAttribute("section", "info");
       /* model.addAttribute("sectionName", "我的资料");
        model.addAttribute("sectionInfo", "绑定邮箱账号后，您可以使用邮箱账号登录本站，也可用邮箱账号找回密码\n");
        model.addAttribute("navtype", "communitynav");*/
        return "/home/user/set";
    }
}
