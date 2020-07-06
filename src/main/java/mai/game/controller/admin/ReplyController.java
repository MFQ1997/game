package mai.game.controller.admin;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  负责管理评论的回复
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-18
 */
@Controller
@RequestMapping("/reply")
public class ReplyController {
    @RequestMapping("/")
    public String index(){
        return "admin/forum/reply/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "admin/forum/reply/add";
    }

    @RequestMapping("/edit")
    public String edit(int id, Model model){

        return "admin/forum/reply/edit";
    }

    /*
    * @description:这是前台用户的回复页面
    * */
    @RequestMapping("/bbs/reply")
    public String homeUserReply(Model model){

        return "home/forum/reply";
    }

}
