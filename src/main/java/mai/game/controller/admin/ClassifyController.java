package mai.game.controller.admin;


import mai.game.entity.po.Classify;
import mai.game.service.ClassifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  这个是板块分类的控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-07
 */
@Controller
@RequestMapping("/classify")
public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;

    @RequestMapping("/")
    public String index(){
        return "/admin/classify/index";
    }

    @RequestMapping("/add")
    public String addPage(){
        return "/admin/classify/add";
    }

    @RequestMapping("/edit/{id}")
    public String editPage(@PathVariable("id") int id, Model model){
        Classify classify = classifyService.getById(id);
        model.addAttribute("classify",classify);
        return "/admin/classify/edit";
    }
}
