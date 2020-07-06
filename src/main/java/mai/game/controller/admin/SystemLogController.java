package mai.game.controller.admin;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-02-19
 */
@Controller
@RequestMapping("/log")
public class SystemLogController {

    @RequestMapping("/index")
    public String index(){
        return "/admin/log/index";
    }

}
