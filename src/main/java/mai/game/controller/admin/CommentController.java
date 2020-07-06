package mai.game.controller.admin;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  负责帖子的评论
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-18
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @RequestMapping("/")
    public String index(){
        return "admin/forum/comment/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "admin/forum/comment/add";
    }

    @RequestMapping("/edit")
    public String edit(int id, Model model){

        return "admin/forum/comment/edit";
    }

}
