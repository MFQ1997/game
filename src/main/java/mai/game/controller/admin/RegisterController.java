package mai.game.controller.admin;

/*
* @Name:RegisterController
* @Description:这个是注册的控制器
* @Date:2020-01-14
* @Author:麦发强
* */

import mai.game.controller.admin.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reg")
public class RegisterController extends BaseController {

    public String register(){
        return "";
    }

}
