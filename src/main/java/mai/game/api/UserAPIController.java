package mai.game.api;

import com.github.pagehelper.PageInfo;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.service.MailService;
import mai.game.core.util.PasswordUtil;
import mai.game.core.util.UploadFileUtil;
import mai.game.entity.mail.Email;
import mai.game.entity.po.Notification;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.User;
import mai.game.entity.vo.AdminUserVO;
import mai.game.entity.vo.UserVO;
import mai.game.service.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*
* @Description:这个是后台管理用户的控制器
* @Date：2020
* */

@RestController
@RequestMapping("/api/user")
public class UserAPIController extends BaseApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UploadFileUtil uploadFileUtil;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ForumModuleService forumModuleService;

    /*
    * @Description:分页获取用户的数据
    * @Param:page 当前分页；row 一页显示的数据条数
    * */
    @GetMapping
    @Log(value="获取用户列表")
    public Response<PageInfo<User>> list(@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        PageInfo<User> usersPage = userService.userPage(page,row);
        //获取数据总条数
        int count = userService.userCount();
        return success("操作成功",0,count,usersPage);
    }

    /*
    * @Description:添加后台管理员
    * */
    @PostMapping
    public Response add(@RequestBody AdminUserVO adminUserVO,HttpServletRequest httpServletRequest){
        System.out.println("添加的管理员的信息是："+adminUserVO);
        AdminUserVO result = userService.addAdminUser(adminUserVO);
        if (result!=null)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    /*
    * @Description：需改管理员的角色
    * */
    @PostMapping("/role/{userId}")
    public Response roleChange(@PathVariable("userId") Integer userId,@RequestBody List<Long> ids){
        boolean result = userRoleService.changeUserRoleByUserId(userId,ids);
        return success("操作成功",0,"success");
    }

    @PutMapping
    @Log(value="修改用户")
    public Response update(@RequestBody User user){
        boolean b = userService.updateById(user);
        if(b)
            return success("操作成功",0,"success");
        return fail("操作失败",1,"fail");
    }

    /*
     * @Description:板块切换状态
     * */
    @PutMapping("/{id}/{status}")
    public Response updateForumModuleStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status){
        SimpleUser user = new SimpleUser();user.setId(id);user.setStatus(status);
        boolean b = userService.updateSimpleById(user);
        if(b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);

    }

    @PutMapping("/reset")
    @Log(value="修改密码")
    @RequiresAuthentication
    public Response resetPassword(@RequestBody SimpleUser userVO){
        System.out.println("修改了密码");
        PasswordUtil passwordHelper = new PasswordUtil();
        if (!passwordHelper.checkResPassword(userVO.getPassword(),userVO.getCheckPassword())){
            return fail("第一次输入的密码和第二次输入的密码不一致",1,null);
        }
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        SimpleUser user = new SimpleUser();
        user.setId(userVO.getId());
        user.setPassword(userVO.getPassword());
        user.setEmail(userEmail);

        //设置修改信息
        //设置加密加盐之后的新密码给user
        passwordHelper.encryptSimpleUserPassword(user);
        String salt = ByteSource.Util.bytes(user.getEmail()).toString();
        user.setSalt(salt);
        boolean b = userService.updatePassword(user);
        if (b)
            return success("操作成功",0,null);
        return fail("操作失败",1,null);

    }

    /*
     * @Description:这个是获取缩略图的上传路径的
     * */
    @PostMapping("/img")
    @RequiresAuthentication
    public Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest){
        System.out.println("进入修改头像");
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        if (userBean == null || userBean.equals("")){
           return fail("用户登录状态异常呢",1,null);
        }
        //获取文件的大小来判断文件上传的大小
        long fileSize = file.getSize();
        //获取文件后缀名字来判断文件类型
        String suffix = "." + file.getContentType().split("/")[1];
        int uploadLimit = Integer.parseInt(systemConfigService.selectAllConfig().get("upload_limit").toString());
        if (!Arrays.asList(".jpg", ".png",".jpeg").contains(suffix.toLowerCase())) {
            return fail("上传失败",1,"上传的格式为："+suffix+",与上传的文件格式不符合"+".jpg .png .jpeg"+"的任何一种");
        }else if (fileSize > uploadLimit * 1024 * 1024) {
            return fail("上传失败",1,"上传的文件大小超过限制的"+uploadLimit+"MB");
        }
        SimpleDateFormat sdf=new SimpleDateFormat("/yyyy-MM-dd");
        String datePath = sdf.format(new Date());
        // 拿到上传后访问的url
        String url = UploadFileUtil.getServerIPPort(httpServletRequest)+uploadFileUtil.upload(file, file.getOriginalFilename(), datePath);
        boolean b = userService.uploadImg(userBean.getId(),url);
        if (b)
            return success("上传成功",0,null);
        return fail("上传失败",1,null);
    }


    @DeleteMapping("/{id}")
    @Log(value = "删除用户")
    public Response delete(@PathVariable("id") int id){
        System.out.println(id);
        userService.removeById(id);
        return success(0,"success");
    }

    @DeleteMapping("/deleteone/{id}")
    @Log(value = "删除资讯")
    public Response deleteOneById(@PathVariable("id") Serializable id){
        boolean b = userService.removeById(id);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    @PostMapping("/batchdel")
    @Log(value = "删除资讯")
    //@RequiresPermissions("article:delete")
    public Response batchdelById(@RequestBody List<Long> ids){
        boolean b = userService.removeByIds(ids);
        if(b)
            return success("删除成功",0,"success");
        return fail("删除失败",1,"fail");
    }

    /*
     * @Description:根据名字进行模糊查询
     * @Param:name(String)
     * @Date:2019
     * */
    @GetMapping("/name/{name}")
    @ResponseBody
    //@RequiresPermissions("user:name")
    public Response<PageInfo<User>> findUserByName(@PathVariable("name") String name,@RequestParam(value="page",defaultValue="1")int page, @RequestParam(value="row",defaultValue="10")int row){
        //List<User> users = userService.findUserByName(name);
        PageInfo<User> pageResult = userService.findUserByLikeName(name,page,row);
        return success(0,pageResult);
    }

    @GetMapping("/id/{id}")
    public Response<User> getUserById(@PathVariable("id") int id){
        System.out.println(id);
        User user = userService.getById(id);
        return success(0,user);

    }

    /*
     * @Description:这个是用户分配角色的操作防范
     * @Date：2019
     * */
    @PostMapping("/allot")
    //@RequiresPermissions("user:allot")
    public Response allotRoleToUser(@RequestBody User user){
        return success(0,"");
    }


    /*
    * @Description:点赞帖子
    * */
    @PostMapping("/like")
    @RequiresAuthentication
    public Response like(@RequestParam("likedTopicId") String likedTopicId){
        System.out.println("进入点赞帖子接口");
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            String userEmail = subject.getPrincipal().toString();
            User userBean = userService.findUserToLogin(userEmail);
            redisService.saveLiked2Redis(likedTopicId, String.valueOf(userBean.getId()));
            redisService.incrementLikedCount(likedTopicId);
            return success("点赞成功",0,null);
        }else if(!subject.isAuthenticated()){
            return fail("请先登录",0,null);
        }
        return fail("操作异常",0,null);
    }
    /*
    * @Description:取消点赞帖子
    * */
    @PostMapping("/unlike")
    @RequiresAuthentication
    public Response unlike(@RequestParam("likedTopicId") String likedTopicId){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            String userEmail = subject.getPrincipal().toString();
            User userBean = userService.findUserToLogin(userEmail);

            redisService.unlikeFromRedis(likedTopicId, String.valueOf(userBean.getId()));
            redisService.decrementLikedCount(String.valueOf(userBean.getId()));
            return success("取消点赞成功",0,null);
        }else if(!subject.isAuthenticated()){
            return fail("请先登录",0,null);
        }
        return fail("操作异常",0,null);
    }


    /*
    * @Description:收藏帖子
    * */
    @PostMapping("/collect")
    @RequiresAuthentication
    public Response collect(@RequestParam("likedTopicId") String likedTopicId){
        System.out.println("进入收藏帖子接口");
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        if (subject.isAuthenticated()) {
            redisService.saveCollect2Redis(likedTopicId, String.valueOf(userBean.getId()));
            redisService.incrementCollectedCount(likedTopicId);
            return success("收藏成功",0,null);
        }else if(!subject.isAuthenticated()){
            return fail("请先登录",0,null);
        }
        return fail("操作异常",0,null);
    }
    /*
    * @Description:取消收藏帖子
    * */
    @PostMapping("/uncollect")
    @RequiresAuthentication
    public Response uncollect(@RequestParam("likedTopicId") String likedTopicId){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        //取消点赞,先存到Redis里面，再定时写到数据库里
        redisService.unCollectFromRedis(likedTopicId, String.valueOf(userBean.getId()));
        redisService.decrementCollectedCount(likedTopicId);
        return success("取消点赞成功",0,null);
    }

    /*
     * @Description:入驻板块
     * */
    @PostMapping("/join")
    @RequiresAuthentication
    public Response join(@RequestParam("forumModuleId") String forumModuleId){
        System.out.println("进入入驻板块接口");
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        System.out.println("入住的帮用户id是："+userBean.getId());
        System.out.println("想要入驻的板块是："+forumModuleId);
        if (subject.isAuthenticated()) {
            redisService.saveJoin2Redis(forumModuleId,String.valueOf(userBean.getId()));
            redisService.incrementJoinedCount(forumModuleId);
            return success("入驻成功",0,null);
        }else if(!subject.isAuthenticated()){
            return fail("入驻失败",0,null);
        }
        return fail("操作异常",0,null);
    }

    /*
     * @Description:取消入驻
     * */
    @PostMapping("/unjoin")
    @RequiresAuthentication
    public Response unjoin(@RequestParam("forumModuleId") String forumModuleId){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        //取消点赞,先存到Redis里面，再定时写到数据库里
        redisService.unJoinFromRedis(forumModuleId,String.valueOf(userBean.getId()));
        redisService.decrementJoinedCount(forumModuleId);
        return success("已取消入驻",0,null);
    }


    /*
     * @Description:关注用户
     * */
    @PostMapping("/follow")
    @RequiresAuthentication
    public Response follow(@RequestParam("followMasterId") String followMasterId){
        System.out.println("进入入驻板块接口");
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        if (userBean.getId().equals(Integer.parseInt(followMasterId)))
            return fail("不需要关注自己的哦",1,null);
        if (subject.isAuthenticated()) {
            redisService.saveFollow2Redis(followMasterId,String.valueOf(userBean.getId()));
            redisService.incrementFollowedCount(followMasterId);
            return success("你已成功关注Ta",0,null);
        }else if(!subject.isAuthenticated()){
            return fail("未登录",0,null);
        }
        return fail("操作异常",0,null);
    }

    /*
     * @Description:取消关注
     * */
    @PostMapping("/unfollow")
    @RequiresAuthentication
    public Response unfollow(@RequestParam("followMasterId") String followMasterId){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        redisService.unFollowFromRedis(followMasterId,String.valueOf(userBean.getId()));
        redisService.decrementFollowedCount(followMasterId);
        return success("已取消关注",0,null);
    }

    @PostMapping("/apply")
    @RequiresAuthentication
    public Response apply(@RequestParam("forumId")Integer forumId
            ,@RequestParam("userId")Integer userId
            ,@RequestParam("reason")String reason){
        System.out.println("申请的板块是："+forumId);
        System.out.println("申请的用户是："+userId);
        System.out.println("申请的理由是"+reason);
        notificationService.insert(userId,forumId,null,"APPLY",reason);
        return success("已提交申请，请耐心等待审核结果哦",0,null);
    }

    @PostMapping("/apply/check")
    public Response applyCheck(@RequestParam("forumId")Integer forumId
            ,@RequestParam("userId")Integer userId
            ,@RequestParam("id")Integer id
            ,@RequestParam("status")Integer status) throws UnsupportedEncodingException {
        SimpleUser simpleUser = userService.selectById(userId);
        if (status.equals(1)){
            //开始处理版主信息
            forumModuleService.setApplyMaster(forumId,userId);
            String content = "恭喜，申请版主成功！！！";
            String email = simpleUser.getEmail();
            //发送邮件通知
            mailService.sendSimpleMail(email, "申请版主-结果通知", content);
        }if (status.equals(2)){
            String content = "抱歉，审核不通过呢。。。";
            String email = simpleUser.getEmail();
            mailService.sendSimpleMail(email, "申请版主-结果通知", content);
        }
        if (!status.equals(0)){
            //开始设置通知信息,将消息设置为已读
            notificationService.markOneReadById(id);
        }
        return  success("操作成功",0,null);
    }
}
