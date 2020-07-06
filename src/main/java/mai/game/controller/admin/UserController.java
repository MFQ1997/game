package mai.game.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.entity.po.Role;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.User;
import mai.game.service.RoleService;
import mai.game.service.SystemConfigService;
import mai.game.service.UserRoleService;
import mai.game.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.security.util.Password;

import javax.management.relation.RoleList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SystemConfigService systemConfigService;

    @RequestMapping("/index")
    public String userIndexPage(Model model){
       /* List<Role> list = roleService.list();
        model.addAttribute("roles",list);*/
        return "admin/user/index";
    }

    @RequestMapping("/add")
    public String userAddPage(Model model){
        List<Role> roleList = roleService.list();
        model.addAttribute("roles", roleList);
        return "admin/user/add";
    }

    @RequestMapping("/edit/{id}")
    public String userEditPage(@PathVariable("id") int userId, Model model){
        //查询出所有的角色
        List<Role> roleList = roleService.list();
        model.addAttribute("roles", roleList);
        //根据用户id查询出所有的角色信息
        List<Integer> userRoleIdList = userRoleService.getRoleListByUserId(userId);
        model.addAttribute("userRoleIdList",userRoleIdList);
        return "admin/user/edit";
    }

    @RequestMapping("/info")
    @RequiresAuthentication
    public String adminUserInfo(Model model){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        if(userEmail==null || userEmail.equals("")){
            return "redirect:/page/admin/login";
        }
        User userBean = userService.findUserToLogin(userEmail);
        System.out.println("当前的登录用户是："+userBean);
        List<Role> roleList = roleService.list();
        model.addAttribute("roles", roleList);
        model.addAttribute("user",userBean);
        String upload_limit = systemConfigService.selectAllConfig().get("upload_limit").toString();
        model.addAttribute("upload_limit",upload_limit);
        return "admin/user/info";
    }

    @RequestMapping("/password")
    @RequiresAuthentication
    public String adminUserPassword(Model model){
        Subject subject = SecurityUtils.getSubject();
        String userEmail = subject.getPrincipal().toString();
        User userBean = userService.findUserToLogin(userEmail);
        System.out.println("当前的登录用户是："+userBean);
        model.addAttribute("userId",userBean.getId());
        return "admin/user/password";
    }


}
