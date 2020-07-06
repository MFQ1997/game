/*
package mai.game.core.handler;

import com.google.common.base.Utf8;
import mai.game.core.service.MailService;
import mai.game.service.SystemConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import static org.springframework.http.HttpStatus.NOT_EXTENDED;

*/
/*
* @Description:全局异常配置
* @Date:2020
* *//*


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Autowired
    private MailService mailService;

    @Autowired
    private SystemConfigService systemConfigService;

    */
/**
     * @Description:在控制器层执行之前，校验Get post方法是否正确之类的操作
     *//*

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("错误");
        return new ResponseEntity<>("出错了", NOT_EXTENDED);
    }

    */
/*
    * @Description:当产生异常的时候就会执行到这个方法，在这个方法里面执行邮件的发送操作
    * *//*

    @ExceptionHandler(value = Exception.class)
    public void jsonHandler(HttpServletRequest request, Exception e) throws Exception {
        log(e, request);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        //发送邮件
        mailService.sendSimpleMail(systemConfigService.selectAllConfig().get("test_email").toString(), "请求："+request.getRequestURL()+" 产生异常", sw.toString());
    }

    private void log(Exception ex, HttpServletRequest request) {
        logger.error("======异常开始======");
        logger.error("请求地址：" + request.getRequestURL());
        Enumeration enumeration = request.getParameterNames();
        logger.error("请求参数");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            logger.error(name + "---" + request.getParameter(name));
        }

        StackTraceElement[] error = ex.getStackTrace();
        for (StackTraceElement stackTraceElement : error) {
            logger.error(stackTraceElement.toString());
        }
        logger.error("======异常结束======");
    }
}
*/
