package mai.game.controller.admin;


import mai.game.service.SystemConfigService;
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
@RequestMapping("/word")
public class SensitiveWordController {

    @Autowired
    private SystemConfigService systemConfigService;

    @RequestMapping("/index")
    public String index(Model model){
        String path = systemConfigService.selectAllConfig().get("word_templet").toString();
        model.addAttribute("path",path);
        return "admin/word/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "admin/word/add";
    }

    @RequestMapping("/import")
    public String importTemplet(){
        return "admin/word/import";
    }

}
