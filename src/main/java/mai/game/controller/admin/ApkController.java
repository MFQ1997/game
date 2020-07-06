package mai.game.controller.admin;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-07
 */
@Controller
@RequestMapping("/apk")
public class ApkController {

    @RequestMapping("/")
    public String index(){
        return "admin/apk/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "admin/apk/upload";
    }
}
