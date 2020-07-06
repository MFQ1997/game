package mai.game.controller.admin;

/*
* @Name:LoginController
* @Description:这个是后台管理用户的登录控制器
* @Date:2020-01-14
* @Author:麦发强
* */

import mai.game.api.BaseApiController;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.util.CodeUtil;
import mai.game.entity.po.User;
import mai.game.entity.vo.UserVO;
import mai.game.service.SystemConfigService;
import mai.game.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/page/mgame")
public class LoginController extends BaseApiController{

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SystemConfigService systemConfigService;

    //用户登录次数计数  redisKey 前缀
    private String SHIRO_LOGIN_COUNT = "shiro_admin_login_count_";
    //用户登录是否被锁定    一小时 redisKey 前缀
    private String SHIRO_IS_LOCK = "shiro_admin_is_lock_";

    @RequestMapping("/login")
    public String loginPage(){
        System.out.println("进入的登录的界面");
        return "admin/user/login";
    }
    /*
     * @Name:login
     * @Description:这个是登录的验证接口,当验证通过之后就将该用户对应的角色所拥有权限返回给前端进行管理
     * @param:User(Class类型)
     * @ReturnType：T
     * */
    @Log(value = "登录")  //这里添加了AOP的自定义注解
    @PostMapping("/api/admin/login")
    @ResponseBody
    public Response<User> adminLogin(@RequestBody UserVO userVO, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        System.out.println("管理员开始登陆");
        System.out.println("后台管理员登录"+userVO);
        if (!CodeUtil.checkVerifyCode(httpServletRequest,userVO.getVercode())){
            return fail("验证码有误",1,null);
        }
        //访问一次，计数一次
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        if ("LOCK".equals(opsForValue.get(SHIRO_IS_LOCK+userVO.getEmail()))){
            return fail("密码错误次数已经超过"+Integer.parseInt(systemConfigService.selectAllConfig().get("login_limit").toString())+"次,账户已锁定,请于"+Integer.parseInt(systemConfigService.selectAllConfig().get("login_limit_time").toString())+"小时后再试",1,null);
        }
        //读取数据库中的登录错误限制时长信息
        opsForValue.increment(SHIRO_LOGIN_COUNT+userVO.getEmail(), 1);
        //计数大于限制次数时，设置用户被锁定一小时
        if(Integer.parseInt(opsForValue.get(SHIRO_LOGIN_COUNT+userVO.getEmail()))>=Integer.parseInt(systemConfigService.selectAllConfig().get("login_limit").toString())){
            opsForValue.set(SHIRO_IS_LOCK+userVO.getEmail(), "LOCK");
            stringRedisTemplate.expire(SHIRO_IS_LOCK+userVO.getEmail(), Integer.parseInt(systemConfigService.selectAllConfig().get("login_limit_time").toString()), TimeUnit.HOURS);
        }

        User user = new User();
        user.setEmail(userVO.getEmail());
        user.setPassword(userVO.getPassword());
        //模拟前端传递过来的用户登录信息
        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(user.getEmail(), user.getPassword());

        System.out.println("token"+usernamePasswordToken);
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            stringRedisTemplate.delete(SHIRO_LOGIN_COUNT+userVO.getEmail());
            stringRedisTemplate.delete(SHIRO_IS_LOCK+userVO.getEmail());

            User userObject = userService.findUserToLogin(user.getEmail());
            User userSession = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
           /* response.sendRedirect("/index/index");*/
            return success("登录成功",0,userObject);
        }catch (UnknownAccountException e){
            return fail("账号不存在",1,null);
        }catch (IncorrectCredentialsException e){
            return fail("密码错误",1,null);
        } catch (DisabledAccountException e){
            return fail("你的账号被锁定了！！！",1,null);
        }catch (AuthenticationException e) {
            System.out.println(e);
            return fail("登录失败",1,null);
        } /*catch (IOException e) {
            return fail("登录失败",1,null);
        }*/
    }


}
