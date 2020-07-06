package mai.game.core.aop;

import com.alibaba.fastjson.JSON;

import mai.game.core.annotation.Log;
import mai.game.core.util.HttpContextUtil;
import mai.game.core.util.IpUtil;
import mai.game.entity.po.SystemLog;
import mai.game.service.SystemLogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private SystemLogService systemLogService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
    @Pointcut("@annotation(mai.game.core.annotation.Log)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        System.out.println("记录日志。。。");
        //保存日志
        SystemLog systemLog = new SystemLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //请求的参数
        Object[] args = joinPoint.getArgs();

        //将参数所在的数组转换成json
        String params = JSON.toJSONString(args[0]);
        systemLog.setParams(params);

        //获取操作
        Log log = method.getAnnotation(Log.class);
        if (log != null) {
            String value = log.value();
            systemLog.setOperation(value);//保存获取的操作
        }

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        systemLog.setMethod(className + "." + methodName);

        Date currentTime = new Date();
        systemLog.setCreateTime(currentTime);
        //获取用户名
        /*String userName=(String) SecurityUtils.getSubject().getPrincipal();
        systemLog.setUserName(userName);*/

        try {
            String userEmail = SecurityUtils.getSubject().getPrincipal().toString() ;
            systemLog.setUserName(userEmail);
        }catch (NullPointerException e){
            systemLog.setUserName("游客登录");
        }

        //获取用户ip地址
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        systemLog.setIp(IpUtil.getIpAddr(request));

        //调用service保存SysLog实体类到数据库
        systemLogService.save(systemLog);
    }
}
