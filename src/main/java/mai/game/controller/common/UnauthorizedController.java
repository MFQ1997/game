package mai.game.controller.common;

import mai.game.api.BaseApiController;
import mai.game.core.bean.Response;
import org.bson.types.Code;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class UnauthorizedController extends BaseApiController {

    @RequestMapping("/unauthorized")
    @ResponseBody
    public Response unauthorized(){
        System.out.println("你没有权限访问");
        return fail("你没有权限！",1,"unPermissted");
    }

    @RequestMapping("/unauthenticated")
    @ResponseBody
    public Response unauthenticated(){
        System.out.println("未认证");
        return fail("未登录！", 1,"unLogin");
    }

    @RequestMapping("/uaz")
    public String uazPage(){
        return "error/uaz";
    }

    @RequestMapping("/uac")
    public String uacPage(){
        return "error/uac";
    }

    @RequestMapping("/mistake")
    @ResponseBody
    public Response error(){
        return fail("异常！", 1,"error");
    }

    @RequestMapping("/valid")
    public void validatedError(@RequestAttribute("msg") String msg, HttpServletResponse response){
        response.setContentType("text/html;charset=UTF-8");
        String error = "<script>alert('"+msg+"');window.history.back();</script> ";
        try {
            response.getWriter().write(error);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
