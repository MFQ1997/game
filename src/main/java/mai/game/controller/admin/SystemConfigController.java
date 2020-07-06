package mai.game.controller.admin;


import mai.game.entity.po.Role;
import mai.game.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-20
 */
@Controller
@RequestMapping("/config")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @RequestMapping("/website")
    public String website(Model model){
        model.addAttribute("systems", systemConfigService.selectAll());
        return "admin/system/website";
    }

    @RequestMapping("/email")
    public String email(){
        return "admin/system/email";
    }


}
