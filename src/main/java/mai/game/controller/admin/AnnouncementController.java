package mai.game.controller.admin;


import mai.game.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-05-08
 */
@Controller
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @RequestMapping("/")
    public String index(Model model){
        return "/admin/announcement/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "/admin/announcement/add";
    }

}
