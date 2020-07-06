package mai.game.controller.admin;


import mai.game.entity.po.FriendUrl;
import mai.game.service.FriendUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 *  友情链接的前端控制器
 * </p>
 *
 * @author 麦发强
 * @since 2020-03-30
 */
@Controller
@RequestMapping("/url")
public class FriendUrlController {

    @Autowired
    private FriendUrlService friendUrlService;

    @RequestMapping("/")
    public String index(){
        return "admin/friend_url/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "admin/friend_url/add";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model){
        FriendUrl url = friendUrlService.getById(id);
        model.addAttribute("url",url);
        return "admin/friend_url/edit";
    }
}
