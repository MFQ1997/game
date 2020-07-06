package mai.game.controller.admin;


import lombok.AllArgsConstructor;
import mai.game.entity.po.ForumModule;
import mai.game.service.ForumModuleService;
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
 * @since 2020-03-02
 */
@Controller
@RequestMapping("/forum/module")
public class ForumModuleController {

    @Autowired
    private ForumModuleService forumModuleService;

    @RequestMapping("/")
    public String index(){
        return "admin/forum/module/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "admin/forum/module/add";
    }

    @RequestMapping("/edit")
    public String edit(int id, Model model){
        ForumModule forumModule = forumModuleService.getById(id);
        model.addAttribute("module",forumModule);
        return "admin/forum/module/edit";
    }

}
