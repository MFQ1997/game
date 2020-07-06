package mai.game.core.exception;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@Controller
public class MyUnauthorizedException implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        System.out.println("==============MyUnauthorizedException=============");
        //如果是shiro无权操作，因为shiro 在操作auno等一部分不进行转发至无权限url
        if(e instanceof UnauthorizedException){
            if (httpServletRequest.getHeader("x-requested-with") != null && httpServletRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                ModelAndView mav = new ModelAndView("redirect:/unauthorized");
                return mav;
            }else{
                ModelAndView mav = new ModelAndView("redirect:/uaz");
                return mav;
            }
        }
        if (e instanceof UnauthenticatedException){
            if (httpServletRequest.getHeader("x-requested-with") != null && httpServletRequest.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
                ModelAndView mav = new ModelAndView("redirect:/unauthenticated");
                return mav;
            }else {
                ModelAndView mav = new ModelAndView("redirect:/uac");
                return mav;
            }
        }
        if (e instanceof ConstraintViolationException){
            ModelAndView  mav = new ModelAndView("forward:/valid");
            String msg = e.getMessage().toString();
            mav.addObject("msg", msg);
            return mav;
        }

        e.printStackTrace();
        System.out.println("==============其他异常=============");
        ModelAndView mv = new ModelAndView("/error");
        mv.addObject("exception", e.toString().replaceAll("\n", "<br/>"));
        return mv;
    }
}
