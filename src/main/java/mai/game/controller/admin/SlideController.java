package mai.game.controller.admin;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器:论坛的轮播图
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-03
 */
@Controller
@RequestMapping("/slide")
public class SlideController {

    @RequestMapping("/")
    public String index(){
        return "/admin/forum/slide/index";
    }

    @RequestMapping("/upload")
    public String upload(){
        return "/admin/forum/slide/upload";
    }
}
