package mai.game.api;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import mai.game.core.annotation.Log;
import mai.game.core.bean.Response;
import mai.game.core.service.MailService;
import mai.game.core.util.*;
import mai.game.entity.po.SimpleUser;
import mai.game.entity.po.User;
import mai.game.entity.vo.UserVO;
import mai.game.service.RedisService;
import mai.game.service.SystemConfigService;
import mai.game.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

@RestController
public class LoginApiController extends BaseApiController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private Producer captchaProducer = null;
    @Autowired
    private MailService mailService;
    @Autowired
    private SystemConfigService systemConfigService;

    //用户登录次数计数  redisKey 前缀
    private String SHIRO_LOGIN_COUNT = "shiro_login_count_";
    //用户登录是否被锁定    一小时 redisKey 前缀
    private String SHIRO_IS_LOCK = "shiro_is_lock_";
    /*
     * @Name:login
     * @Description:这个是登录的验证接口,当验证通过之后就将该用户对应的角色所拥有权限返回给前端进行管理
     * @param:User(Class类型)
     * @ReturnType：T
     * */
    @Log(value = "登录")  //这里添加了AOP的自定义注解
    @PostMapping("/api/login")
    public Response<User> login(@RequestBody UserVO userVO, HttpServletRequest httpServletRequest) {
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
        //System.out.println("token"+usernamePasswordToken);
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
            //登录成功后删除Redis的登录次数记录,
            stringRedisTemplate.delete(SHIRO_LOGIN_COUNT+userVO.getEmail());
            stringRedisTemplate.delete(SHIRO_IS_LOCK+userVO.getEmail());
            //验证通过，获取数据全部数据
            User userObject = userService.findUserToLogin(user.getEmail());
            User userSession = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            System.out.println("从session获取出来的用户"+userSession);

            //Subject subject = SecurityUtils.getSubject();
            //Users users = (Users) subject.getPrincipals();
            //this.request.getSession().setAttribute("userName", Utils.getUser(this.request).getUsername());
            return success("登录成功",0,userObject);
        }catch (UnknownAccountException e){
            return fail("账号不存在",1,null);
        }catch (IncorrectCredentialsException e){

            return fail("密码错误",1,null);
        }catch (DisabledAccountException e){
            return fail("你的账号被锁定了！！！",1,null);
        }catch (AuthenticationException e) {
            System.out.println(e);
            return fail("登录失败",1,null);
        }
    }

    @PostMapping("/code")
    public Response sendVerCodeByEmail(@RequestParam("email") String email,HttpServletRequest request) throws UnsupportedEncodingException {
        if (!ValidateUtil.isEmail(email)){
            return fail("邮箱格式不对",1,null);
        }

            //创建验证码
            String capText = captchaProducer.createText();
            String systemName = systemConfigService.selectAllConfig().get("name").toString();
            String content = "您好：" + email+"欢迎注册" +systemName+
                    "，本次的验证码是："+capText;
            //将生成的验证码写入到session中去
            HttpSession session = request.getSession();
            session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
            mailService.sendSimpleMail(email, "用户注册", content);
            return success("发送成功，请注意查看邮件信息",0,null);
    }

    /*
     * @Name:save()
     * @Description:新增一个用户
     * @Param：User（class）
     * @Date:2019
     * */
    @PostMapping("/api/register")
    @Log(value = "前台用户注册操作")  //这里添加了AOP的自定义注解
    public Response register(@RequestBody UserVO userVO, HttpServletRequest httpServletRequest) {
        if (userService.isNullByEmail(userVO.getEmail())) {
            String regex = "^[a-zA-Z]\\w{5,17}$";
            if (!userVO.getPassword().matches(regex)){
                return fail("密码等级过低，请带有字母和数字和下划线，并且长度6-18位之间", 1, null);
            }
            //获取原始密码
            String pass = userVO.getPassword();
            //检测两次的密码是否一致
            PasswordUtil passwordHelper = new PasswordUtil();
            if (!passwordHelper.checkResPassword(userVO.getPassword(), userVO.getCheckPassword())) {
                return fail("第一次输入的密码和第二次输入的密码不一致", 1, null);
            }
            if (!CodeUtil.checkVerifyCode(httpServletRequest, userVO.getVercode())) {
                return fail("验证码有误", 1, null);
            }
            User user = new User();
            user.setUserName(userVO.getUserName());
            user.setPass(pass);
            user.setPassword(userVO.getPassword());
            user.setEmail(userVO.getEmail());
            user.setStatus(1);
            //查看有没有同名的用户
            try {
                //设置加密加盐之后的新密码给user
                passwordHelper.encryptPassword(user);
                String salt = ByteSource.Util.bytes(user.getEmail()).toString();
                //将新增用户的时候产生的盐值添加到数据库中
                user.setSalt(salt);
                user.setToken(userService.generateToken());
                //新增用户到数据库中
                boolean userAfter = userService.save(user);
                //boolean userAfter = true;
                if (userAfter) {
                    return success("操作成功", 0, user);
                } else {
                    return fail("操作失败", 1, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return fail("操作失败", 1, null);
            }
        }
        return fail("该账号已经注册过了，请直接登录即可",1,null);
    }

/*
     * @Name:save()
     * @Description:新增一个用户
     * @Param：User（class）
     * @Date:2019
     * */
    @PostMapping("/api/forget")
    @Log(value = "前台用户注册操作")  //这里添加了AOP的自定义注解
    public Response forget(@RequestBody UserVO userVO, HttpServletRequest httpServletRequest) {
        System.out.println("忘记密码找回操作");
        PasswordUtil passwordHelper = new PasswordUtil();
        if (!CodeUtil.checkVerifyCode(httpServletRequest,userVO.getVercode())){
            return fail("验证码有误",1,null);
        }
        SimpleUser simpleUserBean = userService.selectSimpleByEmail(userVO.getEmail());

        SimpleUser user = new SimpleUser();
        user.setId(simpleUserBean.getId());
        user.setPassword("000000");
        user.setEmail(simpleUserBean.getEmail());
        //设置修改信息
        //设置加密加盐之后的新密码给user
        passwordHelper.encryptSimpleUserPassword(user);
        String salt = ByteSource.Util.bytes(user.getEmail()).toString();
        user.setSalt(salt);
        boolean b = userService.updatePassword(user);
        if (b)
            return success("密码已经修改为：000000 ，请尽快使用登录修改哦！！！",1,null);
        return fail("操作失败",1,null);

    }


    /*
    * 登出操作
    * */
    @RequestMapping("/logout")
    public ModelAndView forwardMAV(){
        ModelAndView mv = new ModelAndView();
        //手动显式指定使用转发，此时springmvc.xml配置文件中的视图解析器将会失效
        mv.setViewName("/");
        return mv;
    }

    @RequestMapping("/unAuth")
   /* @ResponseStatus(HttpStatus.UNAUTHORIZED)*/
    public Response unauthorized() {
        System.out.println("执行到为授权的链接");
        return fail("UnauthorizedController",1,null);
    }



}
