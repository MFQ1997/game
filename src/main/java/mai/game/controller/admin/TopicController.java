package mai.game.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mai.game.entity.po.Classify;
import mai.game.entity.po.ForumModule;
import mai.game.entity.po.ForumModuleClassify;
import mai.game.entity.po.User;
import mai.game.service.ClassifyService;
import mai.game.service.ForumModuleClassifyService;
import mai.game.service.ForumModuleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
 * @since 2020-03-07
 */
@Controller
@RequestMapping("/topic")
public class TopicController extends BaseController{

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private ForumModuleService forumModuleService;

    @Autowired
    private ForumModuleClassifyService forumModuleClassifyService;

    @RequestMapping("/")
    public String index(){
        return "/admin/forum/topic/index";
    }

    @RequestMapping("/add/{id}")
    public String addTopic(@PathVariable("id") int id, Model model){
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            System.out.println("你已经登录了");
            model.addAttribute("moduleid",id);
            ForumModule byId = forumModuleService.getById(id);
            model.addAttribute("forum",byId);

            //获取板块下的分类信息
            List<ForumModuleClassify> allModuleClassifyByForumModuleId = forumModuleClassifyService.findAllModuleClassifyByForumModuleId(id);
            model.addAttribute("classifies",allModuleClassifyByForumModuleId);
            return "/home/topic/add";
        }
        return "redirect:/my/login";
    }

    @RequestMapping("/vote")
    public String vote(){
        return "/home/forum/topic/vote";
    }

}
