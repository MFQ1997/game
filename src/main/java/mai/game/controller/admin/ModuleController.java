package mai.game.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* @Name:ModuleController
* @Description:这个是论坛的板块管理
* @Date:2020
* @Author:麦发强
* */

@Controller
@RequestMapping("/module")
public class ModuleController {
    public String index(){
        return "/admin/module/index";
    }
}
