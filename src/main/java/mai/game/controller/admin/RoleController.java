package mai.game.controller.admin;

import mai.game.entity.po.Role;
import mai.game.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
* @Name:RoleController
* @Controller:这个是角色管理的控制器
* @Date:2020-01-23
* @Author:麦发强
* */

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{

    @Autowired
    private RoleService roleService;

    /*
    * @Description:这个是角色管理的首页
    * @Date:2020-01-23
    * */
    @RequestMapping("/index")
    public String roleIndexPage(){
        return "admin/role/index";
    }

    /*
    * @Description:这个是角色的添加页面
    * */
    @RequestMapping("/add")
    public String roleAddPage(){
        return "admin/role/add";
    }

    /*
    * @Description:这个是角色的修改页面
    * */
    @RequestMapping("/edit/{id}")
    public String roleEditPage(@PathVariable("id") int id, Model model){
        Role role = roleService.selectById(id);
        model.addAttribute("role",role);
        /*QueryWrapper<Classify> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classify_id",0);
        List<Classify> classifyList = classifyService.list(queryWrapper);*/
        /*model.addAttribute("classifies",classifyList);*/
        return "admin/role/edit";
    }

}
