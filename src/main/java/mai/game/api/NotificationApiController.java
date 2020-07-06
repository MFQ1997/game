package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.bean.Response;
import mai.game.entity.po.Notification;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.Topic;
import mai.game.entity.po.User;
import mai.game.service.NotificationService;
import mai.game.service.UserService;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotificationApiController extends BaseApiController{
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @PostMapping("/notRead")
    public Response<PageInfo<Notification>> message(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="limit",defaultValue="10")int limit) {
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        PageInfo<Notification> list = notificationService.selectNotReadByUserId(userBean.getId(),page,limit);
        System.out.println("??????s获得的通知是："+list);
        return success(list);
    }

    /*
    * @Descrition：标记用户所有的消息为已读
    * */
    @GetMapping("/markAllRead")
    @RequiresAuthentication
    public Response markRead() {
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        notificationService.markAllRead(userBean.getId());
        return success("标记成功",0,null);
    }

    /*
    * @Description:当用户点击查看的时候就将该消息标注为已读
    * */
    @GetMapping("/markOneRead/{id}")
    public Response markOneRead(@PathVariable("id") Integer id) {
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        notificationService.markOneRead(userBean.getId(),id);
        return success("标记成功",0,null);
    }

    @DeleteMapping("/deleteOne/{id}")
    @RequiresAuthentication
    public Response deleteOne(@PathVariable("id") int id){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        boolean result = notificationService.deleteByUserIdAndId(userBean.getId(),id);
        return success("标记成功",0,null);
    }

    @DeleteMapping("/deleteAll")
    public Response deleteAll(){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        //boolean result = notificationService.deleteAllByUserId(userBean.getId());
        return success("标记成功",0,null);
    }



    @GetMapping("/notread/list")
    public Response<PageInfo<Notification>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        // 未读消息列表
        PageInfo<Notification> notificationList = notificationService.selectNotReadByUserId(user.getId(),page,row);
        return success(notificationList);
    }



}
