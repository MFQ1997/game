package mai.game.controller.admin;

/*
* @Description:这个是权限管理的控制器
* @Date:2020-01-23
* @Author:麦发强
* */

import mai.game.entity.po.Permission;
import mai.game.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.ws.soap.Addressing;

@Controller
@RequestMapping("/menu")
public class PermissionController extends BaseController{

    @Autowired
    private PermissionService permissionService;

    /*
    * @Description:这个是权限管理的首页
    * */
    @RequestMapping("/index")
    public String permissionIndexPage(){
        return "admin/permission/index";
    }

    @RequestMapping("/add")
    public String permissionAddPage(){
        return "admin/permission/add";
    }

    @RequestMapping("/edit/{id}")
    public String permissionEditPage(@PathVariable("id")int id, Model model){
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return "admin/permission/edit";
    }
}
