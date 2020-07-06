package mai.game.controller.admin;


import mai.game.entity.po.ForumModule;
import mai.game.entity.po.ForumModuleClassify;
import mai.game.service.ForumModuleClassifyService;
import mai.game.service.ForumModuleService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-04-19
 */
@Controller
@RequestMapping("/forum/module/classify")
public class ForumModuleClassifyController {
    @Autowired
    private ForumModuleClassifyService forumModuleClassifyService;

    @Autowired
    private ForumModuleService forumModuleService;

    @RequestMapping("/")
    public String index(){
        return "admin/forum/module/index";
    }

    /*
    * @DEscription：页面添加分类
    * */
    @RequestMapping("/add/{forumId}")
    @RequiresRoles("超级管理员")
    public String add(@PathVariable("forumId") Integer forumId,Model model){
        ForumModule forumModule = forumModuleService.getById(forumId);
        model.addAttribute("forumModule",forumModule);
        return "admin/forum/module/add-classify";
    }

    /*
    * @description:弹框添加分类
    * */
    @RequestMapping("/add/form/{forumId}")
    public String addform(@PathVariable("forumId") Integer forumId,Model model){
        ForumModule forumModule = forumModuleService.getById(forumId);
        model.addAttribute("forumModule",forumModule);
        return "admin/forum/module/add-classify-form";
    }

    @RequestMapping("/edit/{forumId}")
    public String edit(@PathVariable("forumId") Integer forumId, Model model){
        List<ForumModuleClassify> forumModuleClassifyList = forumModuleClassifyService.list(null);
        model.addAttribute("clist",forumModuleClassifyList);
        ForumModule forumModule = forumModuleService.getById(forumId);
        model.addAttribute("forumModule",forumModule);
        return "admin/forum/module/edit-classify";

    }
}
